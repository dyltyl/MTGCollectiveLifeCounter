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
    public class PlayerControllerTests : TestBase {

        #region Create Player Tests
        [Fact]
        [Trait("Function", "CreatePlayer")]
        public void TestCreatePlayerSuccess() {
            Player player = GeneratePlayer();
            ActionResult<string> result = playerController.CreatePlayer(player);

            Assert.Equal(player.Email, result.Value);
            Player resultPlayer = GetPlayer(player.Email);
            Assert.NotNull(resultPlayer);
            Assert.Equal(player.Email, resultPlayer.Email);
            Assert.Equal(player.Name, resultPlayer.Name);
            Assert.NotEqual(player.Password, resultPlayer.Password); //The password should be obscured
        }

        [Fact]
        [Trait("Function", "CreatePlayer")]
        public void TestCreatePlayerFailurePlayerAlreadyExists() {
            Player player = GeneratePlayer();

            //1st API call
            ActionResult<string> firstResult = playerController.CreatePlayer(player);
            Assert.Equal(player.Email, firstResult.Value);

            //2nd API call
            ActionResult<string> secondResult = playerController.CreatePlayer(player);

            //Assert Failure
            Assert.Null(secondResult.Value);
            ObjectResult objectResult = (ObjectResult)secondResult.Result;
            Assert.Equal((int)HttpStatusCode.Conflict, objectResult.StatusCode);
            Assert.Equal("A user already exists with the username: " + player.Email, (string)objectResult.Value);
        }

        [Fact]
        [Trait("Function", "CreatePlayer")]
        public void TestCreatePlayerFailureNullPlayer() {
            ActionResult<string> result = playerController.CreatePlayer(null);
            Assert.Null(result.Value);
            ObjectResult objectResult = (ObjectResult)result.Result;
            Assert.Equal((int)HttpStatusCode.BadRequest, objectResult.StatusCode);
            Assert.Equal("The player cannot be null", (string)objectResult.Value);
        }
        #endregion
        #region Update Player Tests
        [Fact]
        [Trait("Function", "UpdatePlayer")]
        public void TestUpdatePlayerSuccess() {
            Player player = GeneratePlayer();
            ActionResult<string> createPlayerResult = playerController.CreatePlayer(player);
            Assert.Equal(player.Email, createPlayerResult.Value);
            Player newPlayer = GeneratePlayer();

            ActionResult<Player> result = playerController.UpdatePlayer(player.Email, player.Password, newPlayer);
            Player resultingPlayer = result.Value;
            Assert.NotNull(resultingPlayer);
            Assert.Equal(newPlayer.Email, resultingPlayer.Email);
            Assert.Equal(newPlayer.Name, resultingPlayer.Name);
            Assert.NotEqual(newPlayer.Password, resultingPlayer.Password);
        }

        [Fact]
        [Trait("Function", "UpdatePlayer")]
        public void TestUpdatePlayerFailureWrongCreds() {
            Player player = GeneratePlayer();
            ActionResult<string> createPlayerResult = playerController.CreatePlayer(player);
            Assert.Equal(player.Email, createPlayerResult.Value);
            Player newPlayer = GeneratePlayer();
            ActionResult<Player> result = playerController.UpdatePlayer(newPlayer.Email, newPlayer.Password, newPlayer);
            Player resultingPlayer = result.Value;
            Assert.Null(resultingPlayer);
        }

        [Fact]
        [Trait("Function", "UpdatePlayer")]
        public void TestUpdatePlayerFailureNullPlayer() {
            Player player = GeneratePlayer();
            ActionResult<string> createPlayerResult = playerController.CreatePlayer(player);
            Assert.Equal(player.Email, createPlayerResult.Value);
            ActionResult<Player> result = playerController.UpdatePlayer(player.Email, player.Password, null);
            Player resultingPlayer = result.Value;
            Assert.Null(resultingPlayer);
            ObjectResult objectResult = (ObjectResult)result.Result;
            Assert.Equal((int)HttpStatusCode.BadRequest, objectResult.StatusCode);
            Assert.Equal("Player cannot be null", (string)objectResult.Value);
        }
        
        [Fact]
        [Trait("Function", "UpdatePlayer")]
        public void TestUpdatePlayerFailureNullCreds() {
            Player player = GeneratePlayer();
            ActionResult<string> createPlayerResult = playerController.CreatePlayer(player);
            Assert.Equal(player.Email, createPlayerResult.Value);
            ActionResult<Player> result = playerController.UpdatePlayer(null, null, player);
            ObjectResult objectResult = (ObjectResult)result.Result;
            Assert.Equal((int)HttpStatusCode.BadRequest, objectResult.StatusCode);
            Assert.Equal("User credentials cannot be null", objectResult.Value);
        }
        #endregion
        #region Delete Player Tests
        [Fact]
        [Trait("Function", "DeletePlayer")]
        public void TestDeletePlayerSuccess() {
            Player player = GeneratePlayer();
            ActionResult<string> result = playerController.CreatePlayer(player);
            Assert.Equal(player.Email, result.Value);

            ActionResult<Player> deleteResult = playerController.DeletePlayer(player.Email, player.Password);
            Player resultingPlayer = deleteResult.Value;

            Assert.Equal(player.Email, resultingPlayer.Email);
            Assert.Null(GetPlayer(player.Email));

            createdPlayers.Remove(player);
        }

        [Fact]
        [Trait("Function", "DeletePlayer")]
        public void TestDeletePlayerFailureWrongPassword() {
            Player player = GeneratePlayer();
            ActionResult<string> result = playerController.CreatePlayer(player);
            Assert.Equal(player.Email, result.Value);

            ActionResult<Player> deleteResult = playerController.DeletePlayer(player.Email, "dummy password");
            ObjectResult objectResult = (ObjectResult)deleteResult.Result;
            Assert.Equal((int)HttpStatusCode.BadRequest, objectResult.StatusCode);
            Assert.Equal("Incorrect User Credentials", (string)objectResult.Value);
            Player resultPlayer = GetPlayer(player.Email);
            Assert.NotNull(resultPlayer);
            Assert.Equal(player.Email, resultPlayer.Email);
        }

        [Fact]
        [Trait("Function", "DeletePlayer")]
        public void TestDeletePlayerFailureWrongEmail() {
            Player player = GeneratePlayer();
            ActionResult<string> result = playerController.CreatePlayer(player);
            Assert.Equal(player.Email, result.Value);

            ActionResult<Player> deleteResult = playerController.DeletePlayer("Test user email", player.Password);
            ObjectResult objectResult = (ObjectResult)deleteResult.Result;
            Assert.Equal((int)HttpStatusCode.BadRequest, objectResult.StatusCode);
            Assert.Equal("Incorrect User Credentials", (string)objectResult.Value);
            Player resultPlayer = GetPlayer(player.Email);
            Assert.NotNull(resultPlayer);
            Assert.Equal(player.Email, resultPlayer.Email);
        }

        [Fact]
        [Trait("Function", "DeletePlayer")]
        public void TestDeletePlayerFailureNullEmail() {
            Player player = GeneratePlayer();
            ActionResult<string> result = playerController.CreatePlayer(player);
            Assert.Equal(player.Email, result.Value);

            ActionResult<Player> deleteResult = playerController.DeletePlayer(null, player.Password);
            Assert.Null(deleteResult.Value);
            ObjectResult objectResult = (ObjectResult)deleteResult.Result;
            Assert.Equal((int)HttpStatusCode.BadRequest, objectResult.StatusCode);
            Assert.Equal("Incorrect User Credentials", objectResult.Value);
        }

        [Fact]
        [Trait("Function", "DeletePlayer")]
        public void TestDeletePlayerFailureNullPassword() {
            Player player = GeneratePlayer();
            ActionResult<string> result = playerController.CreatePlayer(player);
            Assert.Equal(player.Email, result.Value);

            ActionResult<Player> deleteResult = playerController.DeletePlayer(player.Email, null);
            Assert.Null(deleteResult.Value);
            ObjectResult objectResult = (ObjectResult)deleteResult.Result;
            Assert.Equal((int)HttpStatusCode.BadRequest, objectResult.StatusCode);
            Assert.Equal("Incorrect User Credentials", objectResult.Value);
        }
    }
    #endregion
}