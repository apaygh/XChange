package com.xeiam.xchange.bitcoinium.service.polling;

import java.io.IOException;

import si.mazi.rescu.RestProxyFactory;

import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.bitcoinium.Bitcoinium;
import com.xeiam.xchange.bitcoinium.BitcoiniumUtils;
import com.xeiam.xchange.bitcoinium.dto.marketdata.BitcoiniumOrderbook;
import com.xeiam.xchange.bitcoinium.dto.marketdata.BitcoiniumTicker;
import com.xeiam.xchange.bitcoinium.dto.marketdata.BitcoiniumTickerHistory;
import com.xeiam.xchange.utils.Assert;

/**
 * <p>
 * Implementation of the raw market data service for Bitcoinium
 * </p>
 * <ul>
 * <li>Provides access to various market data values</li>
 * </ul>
 */
public class BitcoiniumMarketDataServiceRaw extends BitcoiniumBasePollingService {

  private final Bitcoinium bitcoinium;

  /**
   * Constructor
   *
   * @param exchangeSpecification The {@link ExchangeSpecification}
   */
  public BitcoiniumMarketDataServiceRaw(ExchangeSpecification exchangeSpecification) {

    super(exchangeSpecification);
    this.bitcoinium = RestProxyFactory.createProxy(Bitcoinium.class, exchangeSpecification.getSslUri());
  }

  /**
   * @param tradableIdentifier
   * @param currency
   * @param exchange
   * @return a Bitcoinium Ticker object
   * @throws IOException
   */
  public BitcoiniumTicker getBitcoiniumTicker(String tradableIdentifier, String currency) throws IOException {

    String pair = BitcoiniumUtils.createCurrencyPairString(tradableIdentifier, currency);

    // Request data
    BitcoiniumTicker bitcoiniumTicker = bitcoinium.getTicker(pair, exchangeSpecification.getApiKey());

    // Adapt to XChange DTOs
    return bitcoiniumTicker;
  }

  /**
   * @param tradableIdentifier
   * @param currency
   * @param exchange
   * @param timeWindow - The time period of the requested ticker data. Value can be from set: { "10m", "1h", "3h", "12h", "24h", "3d", "7d", "30d", "2M" }
   * @return
   * @throws IOException
   */
  public BitcoiniumTickerHistory getBitcoiniumTickerHistory(String tradableIdentifier, String currency, String timeWindow) throws IOException {

    String pair = BitcoiniumUtils.createCurrencyPairString(tradableIdentifier, currency);

    verifyTimeWindow(timeWindow);

    // Request data
    BitcoiniumTickerHistory bitcoiniumTickerHistory = bitcoinium.getTickerHistory(pair, timeWindow, exchangeSpecification.getApiKey());

    return bitcoiniumTickerHistory;
  }

  /**
   * @param tradableIdentifier
   * @param currency
   * @param exchange
   * @param orderbookwindow - The width of the Orderbook as a percentage plus and minus the current price. Value can be from set: { 2p, 5p, 10p, 20p, 50p, 100p }
   * @return
   */
  public BitcoiniumOrderbook getBitcoiniumOrderbook(String tradableIdentifier, String currency, String orderbookwindow) throws IOException {

    String pair = BitcoiniumUtils.createCurrencyPairString(tradableIdentifier, currency);

    verifyPriceWindow(orderbookwindow);

    // Request data
    BitcoiniumOrderbook bitcoiniumDepth = bitcoinium.getDepth(pair, orderbookwindow, exchangeSpecification.getApiKey());

    return bitcoiniumDepth;
  }

  /**
   * verify
   *
   * @param pair
   */
  private void verifyPriceWindow(String priceWindow) {

    Assert.isTrue(BitcoiniumUtils.isValidPriceWindow(priceWindow), priceWindow + " is not a valid price window!");
  }

  /**
   * verify
   *
   * @param pair
   */
  private void verifyTimeWindow(String timeWindow) {

    Assert.isTrue(BitcoiniumUtils.isValidTimeWindow(timeWindow), timeWindow + " is not a valid time window!");
  }
}
