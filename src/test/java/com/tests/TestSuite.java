package com.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestPlayers.class,
        TestGames.class,
        TestLife.class
})
public class TestSuite {

}
