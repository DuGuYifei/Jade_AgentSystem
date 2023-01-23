# README

## Design
1. Broadcast money
2. Broadcast potion need buy
3. divide price request tasks
4. braodcast price in the market
5. divide potion tasks
6. buy herb
7. buy potion
8. broadcast potion bought
9. broadcast money remain, potion got, herb remain or money earned before delete.

## workflow

1. Mage need request recipe in library and price on the market:
   1. Herbalists have different herbs with different price but unlimited quantity.
   2. Alchemist have different herb backup with different quantity but same price.
2. Mage need but herb firstly, then give them to alchemist to build potion (consume the herbs as recipe).
3. Mage need find the cheapest way - most complex condition:
   1. There is herb in herbalist market and different alchemist backup.
   2. Mage will choose who have cheaper price among these.
   3. More than one alchemist have this herb and cheaper than market, he will choose who has more (make sure he buy as more as possible).
   4. The remain will be bought from herbalist if alchemist doesn't have enough this herb.
   5. Mage need check if his money is enough for buy all herbs plus the money for build potion.
