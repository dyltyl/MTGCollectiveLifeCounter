using Microsoft.AspNetCore.Mvc;
using MTGCollectiveLifeCounterBackend;
using MTGCollectiveLifeCounterBackend.Controllers;
using MTGCollectiveLifeCounterBackend.Models;
using Npgsql;
using System;
using System.Collections.Generic;
using System.Net;
using System.Text;
using Xunit;

namespace BackendTests {
    public class PlayerControllerTests : IDisposable {

        private readonly PlayerController playerController;
        private readonly TestUtility testUtility;

        //Setup
        public PlayerControllerTests() {
            Startup.ConfigureConnectionString();
            playerController = new PlayerController();
            testUtility = new TestUtility();
        }

        //Tear Down
        public void Dispose() {
            foreach(Player player in testUtility.createdPlayers) {
                playerController.DeletePlayer(player.Email, player.Password);
            }
            testUtility.createdPlayers = new List<Player>();
        }

        [Fact]
        public void TestCreatePlayerSuccess() {
            Player player = testUtility.GeneratePlayer();
            ActionResult<string> result = playerController.CreatePlayer(player);

            Assert.Equal(player.Email, result.Value);
            Player resultPlayer = testUtility.GetPlayer(player.Email);
            Assert.NotNull(resultPlayer);
            Assert.Equal(player.Email, resultPlayer.Email);
            Assert.Equal(player.Name, resultPlayer.Name);
            Assert.NotEqual(player.Password, resultPlayer.Password); //The password should be obscured
        }

        [Fact]
        public void TestCreatePlayerFailure() {
            Player player = testUtility.GeneratePlayer();

            //1st API call
            ActionResult<string> firstResult = playerController.CreatePlayer(player);
            Assert.Equal(player.Email, firstResult.Value);

            //2nd API call
            ActionResult<string> secondResult = playerController.CreatePlayer(player);

            //Assert Failure
            ObjectResult objectResult = (ObjectResult)secondResult.Result;
            Assert.Equal((int)HttpStatusCode.Conflict, objectResult.StatusCode);
            Assert.Equal("A user already exists with the username: " + player.Email, (string)objectResult.Value);
        }

        [Fact]
        public void TestDeletePlayerSuccess() {
            Player player = testUtility.GeneratePlayer();
            ActionResult<string> result = playerController.CreatePlayer(player);
            Assert.Equal(player.Email, result.Value);
            Player resultPlayer = testUtility.GetPlayer(player.Email);
            Assert.NotNull(resultPlayer);
            Assert.Equal(player.Email, resultPlayer.Email);

            ActionResult<Player> deleteResult = playerController.DeletePlayer(player.Email, player.Password);
            Player resultingPlayer = deleteResult.Value;

            Assert.Equal(player.Email, resultingPlayer.Email);
            Assert.Null(testUtility.GetPlayer(player.Email));

            testUtility.createdPlayers.Remove(player);

        }
    }
}