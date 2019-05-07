using Backend.Controllers;
using Microsoft.AspNetCore.Mvc;
using MTGCollectiveLifeCounterBackend;
using MTGCollectiveLifeCounterBackend.Models;
using System;
using System.Collections.Generic;
using System.Text;
using Xunit;

namespace BackendTests {
    public class GameControllerTests : TestBase {

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
        
    }
}
