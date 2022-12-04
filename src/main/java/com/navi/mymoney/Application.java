package com.navi.mymoney;

import com.navi.mymoney.input.FileAdapter;
import com.navi.mymoney.model.Portfolio;
import com.navi.mymoney.output.ConsoleAdapter;
import com.navi.mymoney.service.PortfolioManager;
import com.navi.mymoney.service.PortfolioManagerImpl;

public class Application {
    public static void main(String[] args) {
        Portfolio portfolio = new Portfolio();
        PortfolioManager portfolioManager = new PortfolioManagerImpl(portfolio);
        ConsoleAdapter consoleAdapter = new ConsoleAdapter();
        FileAdapter fileAdapter = new FileAdapter(portfolioManager, consoleAdapter);
        fileAdapter.executeCommandsFromFile(args[0]);
    }
}
