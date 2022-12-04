package com.navi.mymoney.input;

import com.navi.mymoney.exception.CannotRebalanceException;
import com.navi.mymoney.model.Fund;
import com.navi.mymoney.output.ConsoleAdapter;
import com.navi.mymoney.service.PortfolioManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Month;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileAdapter {

    private final PortfolioManager portfolioManager;
    private final ConsoleAdapter consoleAdapter;

    public FileAdapter(PortfolioManager portfolioManager, ConsoleAdapter consoleAdapter) {
        this.portfolioManager = portfolioManager;
        this.consoleAdapter = consoleAdapter;
    }

    private void processLine(String line) {
        String[] commandAndInputs = line.split(" ");
        Command command = Command.valueOf(commandAndInputs[0]);

        switch (command) {
            case ALLOCATE:
                validateInputSize(commandAndInputs, Fund.FundType.values().length);
                List<Integer> amounts = getIntegers(1, Fund.FundType.values().length, commandAndInputs);
                Map<Fund, Integer> fundAllocationMap = Map.of(Fund.equity, amounts.get(0), Fund.debt, amounts.get(1), Fund.gold, amounts.get(2));
                portfolioManager.allocate(fundAllocationMap);
                break;
            case SIP:
                validateInputSize(commandAndInputs, Fund.FundType.values().length);
                amounts = getIntegers(1, Fund.FundType.values().length, commandAndInputs);
                Map.of(Fund.equity, amounts.get(0), Fund.debt, amounts.get(1), Fund.gold, amounts.get(2))
                        .forEach(portfolioManager::addSIP);
                break;
            case CHANGE:
                validateInputSize(commandAndInputs, Fund.FundType.values().length + 1);
                List<Double> rates =
                        Arrays.stream(commandAndInputs)
                                .skip(1)
                                .limit(Fund.FundType.values().length)
                                .map(str -> Double.parseDouble(str.replace("%", "")))
                                .collect(Collectors.toList());
                Month month = Month.valueOf(commandAndInputs[Fund.FundType.values().length + 1]);
                Map<Fund, Double> marketChangeMap = Map.of(Fund.equity, rates.get(0), Fund.debt, rates.get(1), Fund.gold, rates.get(2));
                portfolioManager.change(marketChangeMap, month);
                break;
            case BALANCE:
                validateInputSize(commandAndInputs, 1);
                month = Month.valueOf(commandAndInputs[1]);
                consoleAdapter.print(portfolioManager.getBalance(month));
                break;
            case REBALANCE:
                try{
                    consoleAdapter.print(portfolioManager.reBalance());
                } catch (CannotRebalanceException e) {
                    consoleAdapter.print(e.getMsgCode());
                }
                break;
        }
    }

    private void validateInputSize(String[] commandAndInputs, int size) {
        if (commandAndInputs.length != size + 1) {
            throw new InputMismatchException(
                    "Please check the command " + String.join(" ", commandAndInputs));
        }
    }

    private List<Integer> getIntegers(int skip, int limit, String[] commandAndInputs) {
        return Arrays.stream(commandAndInputs)
        .skip(skip)
        .limit(limit)
        .map(Integer::parseInt)
        .collect(Collectors.toList());
    }

    public void executeCommandsFromFile(String filename) throws RuntimeException {
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            lines.forEach(this::processLine);
        } catch (IOException e) {
            throw new RuntimeException("Invalid File. Please check the path & name for input file provided.");
        }
    }
}
