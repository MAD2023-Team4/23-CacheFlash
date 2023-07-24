package sg.edu.np.mad.madasgcacheflash;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CreateDefaultFc {
    ArrayList<Flashcard> flashcardList = new ArrayList<>();
    public ArrayList<Flashcard> createFlashcards() {
        // Create and add flashcards to the flashcardList

        Flashcard france = new Flashcard();
        france.setTitle("France");
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        questions.add("What is the capital of France?");
        questions.add("What is the largest city in France?");
        questions.add("What is the national language of France?");
        answers.add("Paris");
        answers.add("Lyon");
        answers.add("French");
        france.setQuestions(questions);
        france.setAnswers(answers);
        france.setCategory("Social Studies");
        flashcardList.add(france);

        Flashcard math = new Flashcard();
        math.setTitle("Logarithm and Calculus");
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        questions.add("What is the product rule of logarithm?");
        questions.add("What is the quotient rule of logarithm?");
        questions.add("What is the notation for differentiation?");
        answers.add("logb(xy) = logb x + logb y");
        answers.add("loga(x/y)  = loga x – loga y");
        answers.add("dy/dx or f'(x)");
        math.setQuestions(questions);
        math.setAnswers(answers);
        math.setCategory("Math");
        flashcardList.add(math);

        //___________________________________________________________________________________________

        Flashcard socialStudies = new Flashcard();
        socialStudies.setTitle("General Knowledge");
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        questions.add("When did Singapore gain independence?");
        questions.add("Who won the gold medal in Rio 2016?");
        questions.add("Who is the president of Singapore in 2020?");
        questions.add("Who is the Franz Josef Islands named after?");
        questions.add("What is the full name of North Korea?");
        questions.add("What is the real name of Greece (country)");
        questions.add("What very chewy confectionary was banned in sales in Singapore?");
        questions.add("What anthem was played in Singapore before Majulah Singapura was used?");
        questions.add("What is the capital city of Singapore?");
        questions.add("Who was the first prime minister of Singapore?");
        questions.add("What was Singapore previously known as, and starts with 'T'?");
        questions.add("What is the most popular religion in Singapore?");
        questions.add("What is the national flower of Singapore?");
        questions.add("What is the highest point in Singapore, and what is its height?");
        questions.add("What is the name of the strait that separates Singapore from Malaysia?");
        questions.add("What is the name of the longest suspension bridge in Singapore?");
        questions.add("What is the name of the tallest building in Singapore?");
        questions.add("What is the national sport of Singapore?");

        answers.add("1965");
        answers.add("Joseph Schooling");
        answers.add("Mdm Halimah Yacob");
        answers.add("Franz Joseph I");
        answers.add("Democratic People's Republic of Korea");
        answers.add("The Hellenic Republic");
        answers.add("Chewing gum");
        answers.add("God Save the Queen");
        answers.add("Singapore");
        answers.add("Mr Lee Kuan Yew");
        answers.add("Temasek");
        answers.add("Buddhism");
        answers.add("The Vanda Miss Joaqium");
        answers.add("Bukit Timah Hill, 163.66m");
        answers.add("Johor Strait");
        answers.add("Benjamin Sheares Bridge");
        answers.add("Marina Bay Sands SkyPark Observation Deck");
        answers.add("Netball");
        socialStudies.setQuestions(questions);
        socialStudies.setAnswers(answers);
        socialStudies.setCategory("Social Studies");
        flashcardList.add(socialStudies);

        //___________________________________________________________________________________________

        Flashcard economics = new Flashcard();
        economics.setTitle("General Economics");
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        questions.add("What is the study of how people make choices?");
        questions.add("What do economists study?");
        questions.add("What is the basic economic problem?");
        questions.add("What is the law of demand?");
        questions.add("What is the law of supply?");
        questions.add("What is a market?");
        questions.add("What is a market economy?");
        questions.add("What is a command economy?");
        questions.add("What is a mixed economy?");
        questions.add("What is GDP?");
        questions.add("What is inflation?");
        questions.add("What is deflation?");
        questions.add("What is unemployment?");
        questions.add("What is a recession?");
        questions.add("What is a depression?");
        questions.add("What is a boom?");
        questions.add("What is a bubble?");
        questions.add("What is a financial crisis?");
        questions.add("What is a trade deficit?");
        questions.add("What is a trade surplus?");
        questions.add("What is a balance of payments?");
        questions.add("What is a currency?");
        questions.add("What is a central bank?");
        questions.add("What is interest?");
        questions.add("What is inflation targeting?");
        questions.add("What is quantitative easing?");
        questions.add("What is fiscal policy?");
        questions.add("What is a budget deficit?");
        questions.add("What is a budget surplus?");


        answers.add("Economics");
        answers.add("Consumption of goods, services");
        answers.add("Scarcity");
        answers.add("As the price of a good increases, demand for the good decreases");
        answers.add("As the price of a good increases, supply of the good increases");
        answers.add("A place where buyers and sellers of goods and services come together");
        answers.add("An economy where prices of goods & services are determined " +
                "by supply and demand");
        answers.add("An economy where the government controls the prices of goods and services");
        answers.add("An economy that combines elements of a market economy and a command economy");
        answers.add("Gross domestic product, total output of goods and services produced in a country");
        answers.add("A rise in the general level of prices");
        answers.add("A fall in the general level of prices");
        answers.add("How many of people who are willing and able to work but cannot find a job");
        answers.add("Two continuous quarters of negative economic growth");
        answers.add("A severe recession");
        answers.add("A period of rapid economic growth");
        answers.add("A period of rapid price increases in an asset, followed by a crash");
        answers.add("A widespread collapse of the financial system");
        answers.add("When a country imports more goods and services than it exports");
        answers.add("When a country exports more goods and services than it imports");
        answers.add("A record of a country's economic transactions with the rest of the world");
        answers.add("A medium of exchange, unit of account, and store of value");
        answers.add("An institution that manages a country's currency and monetary policy");
        answers.add("The price paid for the use of money");
        answers.add("A monetary policy framework which the central bank aims to keep inflation at target level");
        answers.add("A monetary policy tool which the central bank buys government bonds to increase money");
        answers.add("Government policy that affects the economy, through taxation and spending");
        answers.add("When the government spends more money than it collects in taxes");
        answers.add("When the government collects more money in taxes than it spends");

        //Source from:
        /* Bard, and
        Khan Academy: https://www.khanacademy.org/economics-finance-domain/macroeconomics
        The Economist: https://www.economist.com/
        Investopedia: https://www.investopedia.com/
        The Balance: https://www.thebalance.com/
        Britannica: https://www.britannica.com/
        https://www.bls.gov/ooh/life-physical-and-social-science/economists.htm#:~:text=Economists%20analyze%20topics%20related%20to,%2C%20health%2C%20and%20the%20environment.
         */


        economics.setQuestions(questions);
        economics.setAnswers(answers);
        economics.setCategory("Economics");
        flashcardList.add(economics);

        //___________________________________________________________________________________________

        Flashcard internationalTrade = new Flashcard();
        internationalTrade.setTitle("International Trade");
        questions = new ArrayList<>();
        answers = new ArrayList<>();

        questions.add("What is international trade?");
        answers.add("The exchange of goods, services, and capital across international borders.");

        questions.add("Explain the principle of comparative advantage.");
        answers.add("A country should specialize in producing the goods it can produce most efficiently relative to other countries and trade for goods it cannot produce as efficiently.");

        questions.add("What are the benefits of international trade?");
        answers.add("Increased efficiency, access to a wider variety of goods, and economic growth.");

        questions.add("Define trade surplus.");
        answers.add("When a country exports more goods and services than it imports.");

        questions.add("Explain trade deficit.");
        answers.add("When a country imports more goods and services than it exports.");

        questions.add("What are import tariffs?");
        answers.add("Taxes imposed on imported goods to protect domestic industries and raise revenue for the government.");

        questions.add("Define trade barriers.");
        answers.add("Government-imposed restrictions on international trade, such as tariffs, quotas, and embargoes.");

        questions.add("Explain the concept of trade liberalization.");
        answers.add("The removal or reduction of trade barriers to promote free trade.");

        questions.add("What is a free trade agreement?");
        answers.add("A pact between two or more countries to reduce or eliminate trade barriers among them.");

        questions.add("Define balance of trade.");
        answers.add("The difference between a country's exports and imports of goods and services.");

        questions.add("Explain the role of the World Trade Organization (WTO).");
        answers.add("It is an international organization that deals with global rules of trade between nations.");

        questions.add("What are export subsidies?");
        answers.add("Financial incentives given by governments to domestic companies to encourage exporting.");

        questions.add("Define protectionism.");
        answers.add("The economic policy of restricting trade between countries to protect domestic industries.");

        questions.add("Explain the terms of trade.");
        answers.add("The ratio at which a country can trade its exports for imports from other countries.");

        questions.add("What is a trade bloc?");
        answers.add("A group of countries that join together and eliminate trade barriers among themselves.");

        questions.add("Define foreign direct investment (FDI).");
        answers.add("When a company or individual from one country invests directly in assets in another country.");

        questions.add("Explain the infant industry argument.");
        answers.add("A justification for trade protectionism, stating that young domestic industries need protection to grow and compete with established foreign competitors.");

        questions.add("What are the implications of a devaluation of a country's currency?");
        answers.add("It makes a country's exports cheaper and imports more expensive, potentially improving the trade balance.");

        questions.add("Define trade sanctions.");
        answers.add("Economic penalties imposed by one or more countries against a targeted country to pressure it to change its policies.");

        questions.add("Explain the concept of trade dumping.");
        answers.add("When a country exports goods to another country at a price lower than the production cost to gain a competitive advantage.");

        questions.add("What are trade negotiations?");
        answers.add("Discussions between countries to reach agreements on international trade issues.");

        questions.add("Explain the concept of trade in services.");
        answers.add("The exchange of services, such as banking, tourism, and consulting, between countries.");

        questions.add("Define the terms 'exports' and 'imports'.");
        answers.add("Exports refer to goods and services sold to foreign countries, while imports refer to goods and services bought from foreign countries.");

        questions.add("What is a current account deficit?");
        answers.add("When a country's imports of goods, services, and transfers exceed its exports and income received from abroad.");

        questions.add("Explain the concept of foreign exchange reserves.");
        answers.add("The foreign currencies held by a country's central bank and used to settle international transactions.");
        internationalTrade.setQuestions(questions);
        internationalTrade.setAnswers(answers);
        internationalTrade.setCategory("Economics");
        flashcardList.add(internationalTrade);

        //___________________________________________________________________________________________

        Flashcard monetaryPolicy = new Flashcard();
        monetaryPolicy.setTitle("Monetary Policy");
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        questions.add("What is monetary policy?");
        answers.add("The use of central bank tools to control the money supply and interest rates to achieve economic goals.");

        questions.add("Explain the role of the central bank in monetary policy.");
        answers.add("The central bank is responsible for implementing monetary policy and regulating the banking system.");

        questions.add("What are the main goals of monetary policy?");
        answers.add("Stable prices, full employment, and economic growth.");

        questions.add("Define inflation.");
        answers.add("A sustained increase in the general price level of goods and services in an economy over time.");

        questions.add("Explain the concept of deflation.");
        answers.add("A sustained decrease in the general price level of goods and services in an economy over time.");

        questions.add("What is the Federal Reserve (Fed)?");
        answers.add("The central bank of the United States, responsible for conducting monetary policy.");

        questions.add("Define the discount rate.");
        answers.add("The interest rate at which the central bank lends money to commercial banks.");

        questions.add("Explain open market operations.");
        answers.add("The buying and selling of government securities by the central bank to control the money supply.");

        questions.add("What is the reserve requirement?");
        answers.add("The percentage of deposits that banks are required to hold as reserves.");

        questions.add("Define expansionary monetary policy.");
        answers.add("A policy that aims to stimulate economic growth by increasing the money supply and reducing interest rates.");

        questions.add("Explain contractionary monetary policy.");
        answers.add("A policy that aims to slow down economic growth by reducing the money supply and increasing interest rates.");

        questions.add("What is the Taylor rule?");
        answers.add("A formula that guides central banks in setting the appropriate interest rate based on inflation and economic output.");

        questions.add("Define quantitative easing.");
        answers.add("A monetary policy in which a central bank buys financial assets to increase the money supply.");

        questions.add("Explain the concept of the money multiplier.");
        answers.add("The ratio of the change in the money supply to the change in the monetary base.");

        questions.add("What is the Phillips curve?");
        answers.add("A concept that shows the trade-off between inflation and unemployment.");

        questions.add("Define the natural rate of unemployment.");
        answers.add("The rate of unemployment when the economy is at its potential output.");

        questions.add("Explain the concept of the zero lower bound.");
        answers.add("The lower limit to which interest rates can be reduced, typically close to zero.");

        questions.add("What is the role of the European Central Bank (ECB)?");
        answers.add("The central bank of the Eurozone, responsible for monetary policy and the euro currency.");

        questions.add("Define exchange rate.");
        answers.add("The rate at which one currency can be exchanged for another.");

        questions.add("Explain the concept of a currency peg.");
        answers.add("A fixed exchange rate system in which a country's currency is tied to another currency or a basket of currencies.");

        questions.add("What are the advantages of an independent central bank?");
        answers.add("It can focus on long-term economic stability without political interference.");

        questions.add("Define the real interest rate.");
        answers.add("The nominal interest rate adjusted for inflation.");

        questions.add("Explain the concept of forward guidance in monetary policy.");
        answers.add("Central banks use forward guidance to communicate their future policy intentions to influence expectations and economic behavior.");

        questions.add("What are the challenges of implementing effective monetary policy?");
        answers.add("Lags in policy impact, uncertainty, and potential unintended consequences.");

        questions.add("Define the money supply.");
        answers.add("The total amount of money in circulation in an economy, including cash and various forms of deposits.");

        monetaryPolicy.setQuestions(questions);
        monetaryPolicy.setAnswers(answers);
        monetaryPolicy.setCategory("Economics");
        flashcardList.add(monetaryPolicy);

        //___________________________________________________________________________________________

        Flashcard supplyAndDemand = new Flashcard();
        supplyAndDemand.setTitle("Supply and Demand");
        questions = new ArrayList<>();
        answers = new ArrayList<>();

        questions.add("What is supply?");
        answers.add("Supply refers to the quantity of a product or service that producers are willing and able to provide at various price levels in the market.");

        questions.add("What is demand?");
        answers.add("Demand refers to the quantity of a product or service that consumers are willing and able to buy at different price points in the market.");

        questions.add("How does price affect demand?");
        answers.add("As the price of a product decreases, the quantity demanded usually increases, and vice versa, assuming all other factors remain constant.");

        questions.add("How does price affect supply?");
        answers.add("As the price of a product increases, the quantity supplied generally increases, and vice versa, assuming all other factors remain constant.");

        questions.add("Explain the law of supply and demand.");
        answers.add("The law of supply states that as the price of a good or service increases, the quantity supplied also increases. The law of demand states that as the price of a good or service increases, the quantity demanded decreases, and vice versa.");

        questions.add("What is the equilibrium price?");
        answers.add("The equilibrium price is the market price at which the quantity demanded equals the quantity supplied, resulting in a stable market.");

        questions.add("What happens when there is a surplus in the market?");
        answers.add("A surplus occurs when the quantity supplied exceeds the quantity demanded at the prevailing price. This typically leads to a decrease in price until the surplus is eliminated.");

        questions.add("What happens when there is a shortage in the market?");
        answers.add("A shortage occurs when the quantity demanded exceeds the quantity supplied at the current price. This often results in an increase in price until the shortage is resolved.");

        questions.add("Explain the concept of elasticity of demand.");
        answers.add("Elasticity of demand measures how responsive the quantity demanded of a good or service is to changes in its price. If demand is elastic, a small change in price leads to a proportionately larger change in quantity demanded.");

        questions.add("What are the factors that influence demand?");
        answers.add("Factors influencing demand include consumer preferences, income levels, prices of related goods, population, and advertising.");

        questions.add("What are the factors that influence supply?");
        answers.add("Factors influencing supply include input prices, technology, government policies, and natural disasters.");

        questions.add("Explain the concept of substitute goods.");
        answers.add("Substitute goods are products that can be used in place of one another. When the price of one substitute increases, the demand for the other substitute may increase as consumers switch to the cheaper option.");

        questions.add("Define complementary goods.");
        answers.add("Complementary goods are products that are used together. When the price of one complementary good increases, the demand for the other complementary good may decrease.");

        questions.add("How do changes in income affect demand?");
        answers.add("For most goods, as income increases, the demand for normal goods increases. However, for inferior goods, as income increases, the demand may decrease.");

        questions.add("What are examples of essential factors affecting supply in the market?");
        answers.add("Essential factors affecting supply include changes in input prices (e.g., raw materials, labor), advancements in technology, and government regulations.");

        questions.add("Explain the concept of market equilibrium.");
        answers.add("Market equilibrium occurs when the quantity demanded equals the quantity supplied, resulting in a stable market price.");

        questions.add("What happens when there is a price ceiling?");
        answers.add("A price ceiling is a legal maximum price set by the government, typically to protect consumers from high prices. It may lead to shortages and a black market.");

        questions.add("What happens when there is a price floor?");
        answers.add("A price floor is a legal minimum price set by the government, usually to support producers. It may lead to surpluses and inefficiencies in the market.");

        questions.add("Explain the concept of elasticity of supply.");
        answers.add("Elasticity of supply measures how responsive the quantity supplied is to changes in price. If supply is elastic, a small change in price leads to a proportionately larger change in quantity supplied.");

        questions.add("What is a competitive market?");
        answers.add("A competitive market is a market with many buyers and sellers, where no single buyer or seller can influence the market price.");

        questions.add("How do changes in consumer tastes and preferences affect demand?");
        answers.add("When consumer tastes and preferences for a product increase, the demand for that product may increase.");

        questions.add("What is a price elasticity of demand greater than 1?");
        answers.add("A price elasticity of demand greater than 1 (elastic demand) indicates that demand is highly responsive to changes in price.");

        questions.add("What is a price elasticity of demand less than 1?");
        answers.add("A price elasticity of demand less than 1 (inelastic demand) indicates that demand is less responsive to changes in price.");

        questions.add("What is the cross-price elasticity of demand?");
        answers.add("Cross-price elasticity of demand measures how the quantity demanded of one good changes in response to a change in the price of another good.");

        questions.add("How does technological advancement affect supply?");
        answers.add("Technological advancement often increases supply, as it allows producers to produce more efficiently and at lower costs.");

        supplyAndDemand.setQuestions(questions);
        supplyAndDemand.setAnswers(answers);
        supplyAndDemand.setCategory("Economics");
        flashcardList.add(supplyAndDemand);

        //___________________________________________________________________________________________

        Flashcard sgGeneralKnowledge = new Flashcard();
        sgGeneralKnowledge.setTitle("Singapore General Knowledge");
        questions = new ArrayList<>();
        answers = new ArrayList<>();

        questions.add("What is the official name of Singapore?");
        answers.add("Republic of Singapore");

        questions.add("What are the four official languages of Singapore?");
        answers.add("English, Malay, Mandarin Chinese, and Tamil");

        questions.add("What is the capital city of Singapore?");
        answers.add("Singapore");

        questions.add("When did Singapore gain independence?");
        answers.add("August 9, 1965");

        questions.add("What is the national anthem of Singapore?");
        answers.add("Majulah Singapura");

        questions.add("Who is the current Prime Minister of Singapore?");
        answers.add("Lee Hsien Loong");

        questions.add("Which body of water separates Singapore from Malaysia?");
        answers.add("The Johor Strait");

        questions.add("What is the official currency of Singapore?");
        answers.add("Singapore Dollar (SGD)");

        questions.add("What is the iconic hotel and landmark in Singapore that looks like a ship on top of three towers?");
        answers.add("Marina Bay Sands");

        questions.add("Which famous shopping street in Singapore is known for its wide range of retail and dining options?");
        answers.add("Orchard Road");

        questions.add("What is the name of the world's first nighttime zoo, located in Singapore?");
        answers.add("Night Safari");

        questions.add("Which public housing system in Singapore is known for its unique architectural design and community spaces?");
        answers.add("HDB (Housing and Development Board) flats");

        questions.add("What is the name of the island resort in Singapore that offers attractions like Universal Studios and S.E.A. Aquarium?");
        answers.add("Sentosa Island");

        questions.add("What is the famous landmark in Singapore that resembles a large durian fruit?");
        answers.add("Esplanade - Theatres on the Bay");

        questions.add("Which annual event in Singapore is a grand celebration of lights and colors?");
        answers.add("Singapore's National Day Parade");

        questions.add("What is the name of the iconic Merlion statue, symbolizing Singapore's maritime heritage?");
        answers.add("Merlion Park");

        questions.add("Which cultural district in Singapore is known for its vibrant art scene and colorful murals?");
        answers.add("Haji Lane in Kampong Glam");

        questions.add("What is the primary religion practiced in Singapore?");
        answers.add("Buddhism");

        questions.add("Which famous food center in Singapore is renowned for its diverse and delicious hawker food?");
        answers.add("Maxwell Food Centre");

        questions.add("What is the name of the famous Formula 1 race held annually in Singapore?");
        answers.add("Singapore Grand Prix");

        questions.add("What is the tallest observation wheel in Singapore, offering panoramic views of the city?");
        answers.add("Singapore Flyer");

        questions.add("What is the world's largest indoor waterfall, located within Singapore's Jewel Changi Airport?");
        answers.add("Rain Vortex");

        questions.add("Which historical district in Singapore is known for its well-preserved Peranakan heritage?");
        answers.add("Katong and Joo Chiat");

        questions.add("What is the name of the public transit system in Singapore?");
        answers.add("MRT (Mass Rapid Transit)");

        questions.add("What is the iconic building in Singapore's Central Business District with a unique design resembling a durian?");
        answers.add("ION Orchard");

        questions.add("Which government agency in Singapore is responsible for promoting tourism?");
        answers.add("Singapore Tourism Board (STB)");

        questions.add("What is the famous food in Singapore made of stir-fried rice noodles with eggs, prawns, and bean sprouts?");
        answers.add("Char Kway Teow");

        questions.add("What is the name of the famous bird park in Singapore, home to various bird species?");
        answers.add("Jurong Bird Park");

        questions.add("What is the main island of Singapore connected to by the Causeway and the Second Link?");
        answers.add("Johor, Malaysia");

        questions.add("What is the name of the largest observation deck in Singapore, located at Marina Bay Sands?");
        answers.add("SkyPark Observation Deck");

        sgGeneralKnowledge.setQuestions(questions);
        sgGeneralKnowledge.setAnswers(answers);
        sgGeneralKnowledge.setCategory("Social Studies");
        flashcardList.add(sgGeneralKnowledge);

        //___________________________________________________________________________________________

        Flashcard algebra = new Flashcard();
        algebra.setTitle("Algebra");
        questions = new ArrayList<>();
        answers = new ArrayList<>();

        questions.add("Simplify the expression: 3x + 2x");
        answers.add("5x");

        questions.add("Factor the expression: x^2 - 9");
        answers.add("(x - 3)(x + 3)");

        questions.add("Solve for x: 2x - 7 = 15");
        answers.add("x = 11");

        questions.add("Expand the expression: (2a + 3b)^2");
        answers.add("4a^2 + 12ab + 9b^2");

        questions.add("Simplify the equation: 5(2x + 3) - 4(3x - 2) = 12");
        answers.add("x = 3");

        questions.add("Solve the system of equations: 2x + y = 8, x - y = 2");
        answers.add("x = 3, y = 2");

        questions.add("Factor the quadratic expression: x^2 + 5x + 6");
        answers.add("(x + 2)(x + 3)");

        questions.add("Simplify the expression: 3(x + 2) + 2(2x - 1)");
        answers.add("7x + 4");

        questions.add("Solve for x: 2(3x - 1) + 5 = 17");
        answers.add("x = 3");

        questions.add("Expand and simplify the expression: (3a - 4b)^2");
        answers.add("9a^2 - 24ab + 16b^2");

        questions.add("Solve the equation: 4x + 7 = 3x - 2");
        answers.add("x = -9");

        questions.add("Factor the trinomial: x^2 + 7x + 10");
        answers.add("(x + 5)(x + 2)");

        questions.add("Simplify the expression: 2(4x - 3) + 3(x + 2)");
        answers.add("11x + 1");

        questions.add("Solve for x: 3(x - 2) = 9 - 2x");
        answers.add("x = 3");

        questions.add("Factor the quadratic expression: 2x^2 - 8x");
        answers.add("2x(x - 4)");

        questions.add("Simplify the expression: 5(x + 3) - 2(2x - 5)");
        answers.add("x + 23");

        questions.add("Solve the system of equations: \n3x + y = 10\n2x - y = 4");
        answers.add("x = 2, y = 4");

        questions.add("Factor the trinomial: x^2 - 6x + 9");
        answers.add("(x - 3)^2");

        questions.add("Simplify the expression: 4(2x - 3) - 2(x + 5)");
        answers.add("6x - 22");

        questions.add("Solve for x: 3(2x + 1) = 15");
        answers.add("x = 2");

        questions.add("Factor the quadratic expression: 3x^2 + 12x + 9");
        answers.add("3(x + 3)^2");

        questions.add("Simplify the expression: 2(3x - 4) - 3(x - 1)");
        answers.add("x - 5");

        questions.add("Solve the equation: 5x - 7 = 8 - 2x");
        answers.add("x = 1");

        questions.add("Factor the trinomial: x^2 + 10x + 21");
        answers.add("(x + 7)(x + 3)");

        questions.add("Simplify the expression: 3(4x + 5) - 2(2x - 1)");
        answers.add("10x + 13");

        algebra.setQuestions(questions);
        algebra.setAnswers(answers);
        algebra.setCategory("Math");
        flashcardList.add(algebra);

        return flashcardList;
    }
}
