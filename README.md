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
