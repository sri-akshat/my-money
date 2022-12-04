# my-money

## Problem Statement

https://codu.ai/coding-problem/mymoney

## Requirements
JDK version 11

## Local Setup
##### Build jar

`mvn clean install -DskipTests -q assembly:single`

##### Run

`java -jar target/geektrust.jar <input-file-path>`

Or

Run Application.java in IDE

Input 1

    ALLOCATE 6000 3000 1000
    SIP 2000 1000 500
    CHANGE 4.00% 10.00% 2.00% JANUARY
    CHANGE -10.00% 40.00% 0.00% FEBRUARY
    CHANGE 12.50% 12.50% 12.50% MARCH
    CHANGE 8.00% -3.00% 7.00% APRIL
    CHANGE 13.00% 21.00% 10.50% MAY
    CHANGE 10.00% 8.00% -5.00% JUNE
    BALANCE MARCH
    REBALANCE

------------
Output 1

    (base) akshat.s@C02DF3ZBMD6R MyMoney % mvn clean install -DskipTests -q assembly:single                                               
    (base) akshat.s@C02DF3ZBMD6R MyMoney % java -jar target/geektrust.jar "/Users/akshat.s/Machine Round/MyMoney/src/main/resources/input"
    10593 7897 2272 
    11809 23619 3936 

------------
Input 2

    ALLOCATE 8000 6000 3500
    SIP 3000 2000 1000
    CHANGE 11.00% 9.00% 4.00% JANUARY
    CHANGE -6.00% 21.00% -3.00% FEBRUARY
    CHANGE 12.50% 18.00% 12.50% MARCH
    CHANGE 23.00% -3.00% 7.00% APRIL
    BALANCE MARCH
    BALANCE APRIL
    REBALANCE

------------
Output 2

    (base) akshat.s@C02DF3ZBMD6R MyMoney % java -jar target/geektrust.jar "/Users/akshat.s/Machine Round/MyMoney/src/main/resources/input"
    15937 14552 6187 
    23292 16055 7690 
    CANNOT_REBALANCE

##### Test

`mvn clean test`
`mvn jacoco:report `

## Domain Entities

**Investor** - User who invests

**Portfolio** - An investor has a portfolio

**FundAccount** - A portfolio is composed of multiple fund accounts

**Fund** - Investment instruments e.g. Gold, Equity, Debt Funds. Also holds on to the market change values e.g. Gold fund knows its % change month on month

**Transaction** - A FundAccount has multiple transactions (BUY / SELL / ADJUSTMENT)

**SIP** - An investor invests into a Fund through a SIP (systemic investment plan)


## Design Considerations

#### Scale

Since the getBalance() behaviour calculates the balance everytime from the beginning, caching the balance calculation at a month granularity can help optimize on compute time. The underlying assumption is that history is immutable (think of it as a bank passbook)

The cache should be cleared on reBalance() or change() invocation to ensure integrity

#### Race Condition

**Case 1**: getBalance() is invoked along with a reBalance() or change(), can lead to dirty reads

**Case 2** : reBalance() is invoked along with change(), this can lead to incorrect reBalancing

We can avoid the race condition by taking a lock on FundAccount, before reBalancing or change()

#### Architecture

Hexagonal architecture - Idea is to have a clean business layer, separated from Input / Output logic

https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)
