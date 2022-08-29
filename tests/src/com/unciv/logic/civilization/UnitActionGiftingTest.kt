package com.unciv.logic.civilization

import com.badlogic.gdx.math.Vector2
import com.unciv.Constants
import com.unciv.logic.trade.Trade
import com.unciv.logic.trade.TradeOffer
import com.unciv.logic.trade.TradeType
import com.unciv.testing.GdxTestRunner
import com.unciv.ui.worldscreen.unit.UnitActions
import com.unciv.uniques.TestGame
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(GdxTestRunner::class)
class UnitActionGiftingTest {
    private lateinit var game: TestGame

    @Before
    fun initTheWorld() {
        game = TestGame()
        game.makeRectangularMap(15,15, Constants.desert)
    }

    //If unit gifter and unit recipient both traded open borders, and the unit is within the recipient's borders,
    //  then the gift action should be available.
    @Test
    fun giftUnitBothSidesOpenBorders() {
        val civInfo = game.addCiv()
        val tile = game.setTileFeatures(Vector2(2f,2f), Constants.desert)
        val cityInfo = game.addCity(civInfo, tile, true)

        val otherCivInfo = game.addCiv()
        val otherCivCityTile = game.setTileFeatures(Vector2(0f,0f), Constants.desert)
        val otherCivCityInfo = game.addCity(otherCivInfo, otherCivCityTile, true)
        val otherCivExtraCityTile = game.setTileFeatures(Vector2(1f,0f), Constants.desert)
        game.addTileToCity(otherCivCityInfo, otherCivExtraCityTile)
        otherCivCityInfo.cityStats.update()

        //Set up open borders trade
        val civTradeOffer = TradeOffer("Open Borders",TradeType.Agreement,1,30)
        val otherCivTradeOffer = TradeOffer("Open Borders",TradeType.Agreement,1,30)
        val trade = Trade()
        trade.ourOffers.add(civTradeOffer)
        trade.theirOffers.add(otherCivTradeOffer)
        civInfo.getDiplomacyManager(otherCivInfo).trades.add(trade)
        civInfo.getDiplomacyManager(otherCivInfo).updateHasOpenBorders()

        //Add a unit to gift
        val giftUnit = game.addUnit("Warrior", civInfo, tile)

        val giftAction = UnitActions.getGiftAction(giftUnit, otherCivExtraCityTile)

        Assert.assertTrue("giftAction should not be null", giftAction != null)
    }

    //If only the unit gifter traded open borders, then the gift action should not be available.
    @Test
    fun giftUnitGifterOnlyOpenBorders() {
        val civInfo = game.addCiv()
        val tile = game.setTileFeatures(Vector2(2f,2f), Constants.desert)
        val cityInfo = game.addCity(civInfo, tile, true)

        val otherCivInfo = game.addCiv()
        val otherCivCityTile = game.setTileFeatures(Vector2(0f,0f), Constants.desert)
        val otherCivCityInfo = game.addCity(otherCivInfo, otherCivCityTile, true)
        val otherCivExtraCityTile = game.setTileFeatures(Vector2(1f,0f), Constants.desert)
        game.addTileToCity(otherCivCityInfo, otherCivExtraCityTile)
        otherCivCityInfo.cityStats.update()

        //Set up open borders trade - Unit gifter side only
        val civTradeOffer = TradeOffer("Open Borders",TradeType.Agreement,1,30)
        val trade = Trade()
        trade.ourOffers.add(civTradeOffer)
        civInfo.getDiplomacyManager(otherCivInfo).trades.add(trade)
        civInfo.getDiplomacyManager(otherCivInfo).updateHasOpenBorders()

        //Add a unit to gift
        val giftUnit = game.addUnit("Warrior", civInfo, tile)

        val giftAction = UnitActions.getGiftAction(giftUnit, otherCivExtraCityTile)

        Assert.assertTrue("giftAction should be null", giftAction == null)
    }

    //If only the unit recipient traded open borders, and the unit is within the recipient's borders,
    //  then the gift action should be available.
    @Test
    fun giftUnitRecipientOnlyOpenBorders() {
        val civInfo = game.addCiv()
        val tile = game.setTileFeatures(Vector2(2f,2f), Constants.desert)
        val cityInfo = game.addCity(civInfo, tile, true)

        val otherCivInfo = game.addCiv()
        val otherCivCityTile = game.setTileFeatures(Vector2(0f,0f), Constants.desert)
        val otherCivCityInfo = game.addCity(otherCivInfo, otherCivCityTile, true)
        val otherCivExtraCityTile = game.setTileFeatures(Vector2(1f,0f), Constants.desert)
        game.addTileToCity(otherCivCityInfo, otherCivExtraCityTile)
        otherCivCityInfo.cityStats.update()

        //Set up open borders trade
        val otherCivTradeOffer = TradeOffer("Open Borders",TradeType.Agreement,1,30)
        val trade = Trade()
        trade.theirOffers.add(otherCivTradeOffer)
        civInfo.getDiplomacyManager(otherCivInfo).trades.add(trade)
        civInfo.getDiplomacyManager(otherCivInfo).updateHasOpenBorders()

        //Add a unit to gift
        val giftUnit = game.addUnit("Warrior", civInfo, tile)

        val giftAction = UnitActions.getGiftAction(giftUnit, otherCivExtraCityTile)

        Assert.assertTrue("giftAction should not be null", giftAction != null)
    }

    //If unit gifter and unit recipient both traded open borders, and the unit is outside of the recipient's borders,
    //  then the gift action should not be available.
    @Test
    fun giftUnitOutsideOfOpenBorders() {
        val civInfo = game.addCiv()
        val tile = game.setTileFeatures(Vector2(2f,2f), Constants.desert)
        val cityInfo = game.addCity(civInfo, tile, true)

        val otherCivInfo = game.addCiv()
        val otherCivCityTile = game.setTileFeatures(Vector2(0f,0f), Constants.desert)
        val otherCivCityInfo = game.addCity(otherCivInfo, otherCivCityTile, true)
        val otherCivExtraCityTile = game.setTileFeatures(Vector2(1f,0f), Constants.desert)
        game.addTileToCity(otherCivCityInfo, otherCivExtraCityTile)
        otherCivCityInfo.cityStats.update()

        //Create tile that is not within a city's territory
        val unownedTile = game.setTileFeatures(Vector2(7f,7f), Constants.desert)

        //Set up open borders trade
        val civTradeOffer = TradeOffer("Open Borders",TradeType.Agreement,1,30)
        val otherCivTradeOffer = TradeOffer("Open Borders",TradeType.Agreement,1,30)
        val trade = Trade()
        trade.ourOffers.add(civTradeOffer)
        trade.theirOffers.add(otherCivTradeOffer)
        civInfo.getDiplomacyManager(otherCivInfo).trades.add(trade)
        civInfo.getDiplomacyManager(otherCivInfo).updateHasOpenBorders()

        //Add a unit to gift
        val giftUnit = game.addUnit("Warrior", civInfo, tile)

        val giftAction = UnitActions.getGiftAction(giftUnit, unownedTile)

        Assert.assertTrue("giftAction should be null", giftAction == null)
    }

}
