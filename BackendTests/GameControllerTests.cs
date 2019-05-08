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
        #endregion

    }
}
