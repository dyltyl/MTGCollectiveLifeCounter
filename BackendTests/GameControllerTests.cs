using Backend.Controllers;
using Microsoft.AspNetCore.Mvc;
using MTGCollectiveLifeCounterBackend;
using MTGCollectiveLifeCounterBackend.Models;
using System;
using System.Collections.Generic;
using System.Net;
using System.Text;
using Xunit;

namespace BackendTests {
    public class GameControllerTests : TestBase {

        #region Create Game Tests
        [Fact]
        [Trait("Function", "CreateGame")]
        public void TestCreateGameSuccess() {
            Game game = GenerateGame();
            ActionResult<string> result = gameController.CreateGame(game);
            Assert.Equal(game.GameId, result.Value);
            Game resultingGame = GetGame(game.GameId);
            Assert.NotNull(resultingGame);
            Assert.Equal(game.GameId, resultingGame.GameId);
            Assert.NotEqual(game.GamePassword, resultingGame.GamePassword);
        }
        [Fact]
        public void TestCreateGameFailureNullGame() {
            ActionResult<string> result = gameController.CreateGame(null);
            ObjectResult objectResult = (ObjectResult)result.Result;
            Assert.Equal((int)HttpStatusCode.BadRequest, objectResult.StatusCode);
            Assert.Equal("Game cannot be null", objectResult.Value);
        }
        [Fact]
        [Trait("Function", "CreateGame")]
        public void TestCreateGameFailureGameAlreadyExists() {
            Game game = GenerateGame();
            ActionResult<string> result = gameController.CreateGame(game);
            Assert.Equal(game.GameId, result.Value);

            result = gameController.CreateGame(game);
            ObjectResult objectResult = (ObjectResult)result.Result;
            Assert.Equal((int)HttpStatusCode.Conflict, objectResult.StatusCode);
            Assert.Equal("Cannot create game, it already exists", objectResult.Value);
        }
        #endregion
        #region Update Game Tests
        [Fact]
        [Trait("Function", "UpdateGame")]
        public void TestUpdateGameSuccess() {
            Game game = GenerateGame();
            ActionResult<string> createResult = gameController.CreateGame(game);
            Assert.Equal(game.GameId, createResult.Value);
            Game newGame = GenerateGame();

            ActionResult<Game> result = gameController.UpdateGame(game.GameId, game.GamePassword, newGame);
            Game resultingGame = result.Value;
            Assert.NotNull(resultingGame);
            Assert.Equal(newGame.GameId, resultingGame.GameId);
        }
        [Fact]
        [Trait("Function", "UpdateGame")]
        public void TestUpdateGameFailureNullGame() {
            Game game = GenerateGame();
            ActionResult<string> createResult = gameController.CreateGame(game);
            Assert.Equal(game.GameId, createResult.Value);

            ActionResult<Game> result = gameController.UpdateGame(game.GameId, game.GamePassword, null);
            ObjectResult objectResult = (ObjectResult)result.Result;
            Assert.Equal((int)HttpStatusCode.BadRequest, objectResult.StatusCode);
            Assert.Equal("Player cannot be null", objectResult.Value);
        }
        [Fact]
        [Trait("Function", "UpdateGame")]
        public void TestUpdateGameFailureNullGameId() {
            Game game = GenerateGame();
            ActionResult<string> createResult = gameController.CreateGame(game);
            Assert.Equal(game.GameId, createResult.Value);

            Game newGame = GenerateGame();
            ActionResult<Game> result = gameController.UpdateGame(null, game.GamePassword, newGame);
            ObjectResult objectResult = (ObjectResult)result.Result;
            Assert.Equal((int)HttpStatusCode.BadRequest, objectResult.StatusCode);
            Assert.Equal("Game credentials cannot be null", objectResult.Value);
        }
        [Fact]
        [Trait("Function", "UpdateGame")]
        public void TestUpdateGameFailureNullGamePassword() {
            Game game = GenerateGame();
            ActionResult<string> createResult = gameController.CreateGame(game);
            Assert.Equal(game.GameId, createResult.Value);

            Game newGame = GenerateGame();
            ActionResult<Game> result = gameController.UpdateGame(game.GameId, null, newGame);
            ObjectResult objectResult = (ObjectResult)result.Result;
            Assert.Equal((int)HttpStatusCode.BadRequest, objectResult.StatusCode);
            Assert.Equal("Game credentials cannot be null", objectResult.Value);
        }
        [Fact]
        [Trait("Function", "UpdateGame")]
        public void TestUpdateGameFailureWrongCreds() {
            Game game = GenerateGame();
            ActionResult<string> createResult = gameController.CreateGame(game);
            Assert.Equal(game.GameId, createResult.Value);
            
            Game newGame = GenerateGame();
            ActionResult<Game> result = gameController.UpdateGame("Not the username", "Not the password", newGame);
            ObjectResult objectResult = (ObjectResult)result.Result;
            Assert.Equal((int)HttpStatusCode.BadRequest, objectResult.StatusCode);
            Assert.Equal("Incorrect Game Credentials", objectResult.Value);
        }
        #endregion
        #region Delete Game Tests
        [Fact]
        [Trait("Function", "DeleteGame")]
        public void TestDeleteGameSuccess() {
            Game game = GenerateGame();
            ActionResult<string> result = gameController.CreateGame(game);
            Assert.Equal(game.GameId, result.Value);

            ActionResult<Game> deleteResult = gameController.DeleteGame(game.GameId, game.GamePassword);
            Game resultingGame = deleteResult.Value;
            Assert.NotNull(resultingGame);
            Assert.Equal(game.GameId, resultingGame.GameId);
            Assert.NotEqual(game.GamePassword, resultingGame.GamePassword);

            try {
                Assert.Null(GetGame(game.GameId));
            }
            catch(ArgumentException) {
                Assert.True(true);
            }
        }
        [Theory]
        [Trait("Function", "DeleteGame")]
        [InlineData(null, null)]
        [InlineData("TestGameId", null)]
        [InlineData(null, "TestGamePassword")]
        [InlineData("FakeGameId", "FakeGamePassword")]
        public void TestDeleteGameFailure(string gameId, string gamePassword) {
            Game game = GenerateGame();
            game.GameId = "TestGameId";
            game.GamePassword = "TestGamePassword";
            ActionResult<string> createResult = gameController.CreateGame(game);
            Assert.Equal(game.GameId, createResult.Value);

            ActionResult<Game> result = gameController.UpdateGame(gameId, gamePassword, game);
            ObjectResult objectResult = (ObjectResult)result.Result;
            Assert.Equal((int)HttpStatusCode.BadRequest, objectResult.StatusCode);
            if (gameId != null && gamePassword != null)
                Assert.Equal("Incorrect Game Credentials", objectResult.Value);
            else
                Assert.Equal("Game credentials cannot be null", objectResult.Value);
        }
        #endregion

    }
}
