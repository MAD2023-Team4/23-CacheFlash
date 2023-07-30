package sg.edu.np.mad.madasgcacheflash;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CreateDefaultFc {
    ArrayList<Flashcard> flashcardList = new ArrayList<>();

    //Feel free to comment out (turn off) a method to create a flashcard, if it is not used to
    //create a flashcard.
    //___________________________________________________________________________________________
    public ArrayList<Flashcard> createFlashcards() {
        // Create and add flashcards to the flashcardList
        createFrance();
        createLogAndCalc();
        createGeneralKnowledge();
        createGeneralEcons();
        createInternationalTrade();
        createMonetaryPolicy();
        createSupplyAndDemand();
        createSgGeneralKnowledge();
        createAlgebra();

        return flashcardList;
    }


    //The following code can create individual flashcards.
    //______________________________________________________________________________________________
    public void createFrance(){
        Flashcard france = new Flashcard();
        france.setTitle("France");
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        questions.add("What is the capital of France?");
        questions.add("What is the national language of France?");
        questions.add("What river runs through Paris?");
        questions.add("Which famous art museum is located in Paris?");
        questions.add("What is the national anthem of France?");
        questions.add("Which French holiday is celebrated on July 14th?");
        questions.add("What is the name of the famous Gothic cathedral in Paris?");
        questions.add("Which French fashion designer founded the luxury brand Chanel?");
        questions.add("What is the French currency called?");
        questions.add("What iconic tower is in Paris, offering panoramic city views?");

        answers.add("Paris");
        answers.add("French");
        answers.add("Seine");
        answers.add("Louvre");
        answers.add("La Marseillaise");
        answers.add("Bastille Day");
        answers.add("Notre-Dame");
        answers.add("Coco Chanel");
        answers.add("Euro");
        answers.add("Eiffel Tower");

        france.setQuestions(questions);
        france.setAnswers(answers);
        france.setCategory("Social Studies");
        france.setImageResourceId(R.drawable.france);
        flashcardList.add(france);
    }

    public void createLogAndCalc(){
        Flashcard math = new Flashcard();
        math.setTitle("Prime Factorisation");
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();

        // Questions
        questions.add("What is the prime factorization of the perfect square 784?");
        questions.add("What is the prime factorization of the perfect square 1089?");
        questions.add("What is the prime factorization of the perfect square 1764?");
        questions.add("What is the prime factorization of the perfect square 2116?");
        questions.add("What is the prime factorization of the perfect square 2916?");
        questions.add("What is the prime factorization of the perfect square 4356?");
        questions.add("What is the prime factorization of the perfect square 5041?");
        questions.add("What is the prime factorization of the perfect square 6241?");
        questions.add("What is the prime factorization of the perfect square 7744?");
        questions.add("What is the prime factorization of the perfect square 8464?");

        // Answers
        answers.add("2^4 * 7^2");    // Prime factorization of 784 (2^4 * 7^2)
        answers.add("3^2 * 11^2");   // Prime factorization of 1089 (3^2 * 11^2)
        answers.add("2^2 * 3^2 * 7^2");    // Prime factorization of 1764 (2^2 * 3^2 * 7^2)
        answers.add("2^2 * 29^2");    // Prime factorization of 2116 (2^2 * 29^2)
        answers.add("2^2 * 3^4");    // Prime factorization of 2916 (2^2 * 3^4)
        answers.add("2^2 * 3^2 * 11^2");    // Prime factorization of 4356 (2^2 * 3^2 * 11^2)
        answers.add("71^2");    // Prime factorization of 5041 (71^2)
        answers.add("79^2");    // Prime factorization of 6241 (79^2)
        answers.add("2^8 * 19^2");    // Prime factorization of 7744 (2^8 * 19^2)
        answers.add("2^8 * 13^2");    // Prime factorization of 8464 (2^8 * 13^2)

        //inspired by chatgpt, and
        // https://study.com/academy/flashcards/perfect-squares-list-flashcards.html

        math.setQuestions(questions);
        math.setAnswers(answers);
        math.setCategory("Math");
        math.setImageResourceId(R.drawable.math);
        flashcardList.add(math);
    }

    public void createGeneralKnowledge(){
        Flashcard socialStudies = new Flashcard();
        socialStudies.setTitle("General Knowledge");
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();

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
        socialStudies.setImageResourceId(R.drawable.sgss);
        flashcardList.add(socialStudies);
    }

    public void createGeneralEcons(){
        Flashcard economics = new Flashcard();
        economics.setTitle("General Economics");
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();

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
        economics.setImageResourceId(R.drawable.economics);
        flashcardList.add(economics);
    }

    public void createInternationalTrade(){
        Flashcard internationalTrade = new Flashcard();
        internationalTrade.setTitle("International Trade");
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();

        questions.add("What is international trade?");
        answers.add("Exchange of goods, services, and capital across borders.");

        questions.add("Explain the principle of comparative advantage.");
        answers.add("Countries specialize efficiently and trade accordingly.");

        questions.add("What are the benefits of international trade?");
        answers.add("Increased efficiency, wider variety of goods, economic growth.");

        questions.add("Define trade surplus.");
        answers.add("Exports > imports.");

        questions.add("Explain trade deficit.");
        answers.add("Imports > exports.");

        questions.add("What are import tariffs?");
        answers.add("Taxes on imported goods, protect domestic industries, raise revenue.");

        questions.add("Define trade barriers.");
        answers.add("Government restrictions on trade, e.g., tariffs, quotas, embargoes.");

        questions.add("Explain the concept of trade liberalization.");
        answers.add("Removing trade barriers to promote free trade.");

        questions.add("What is a free trade agreement?");
        answers.add("Pact to reduce/eliminate trade barriers between countries.");

        questions.add("Define balance of trade.");
        answers.add("Exports - imports of goods and services.");

        questions.add("Explain the role of the World Trade Organization (WTO).");
        answers.add("Handles global trade rules between nations.");

        questions.add("What are export subsidies?");
        answers.add("Incentives for domestic companies to encourage exporting.");

        questions.add("Define protectionism.");
        answers.add("Restricting trade to protect domestic industries.");

        questions.add("Explain the terms of trade.");
        answers.add("Ratio of a country's exports for imports.");

        questions.add("What is a trade bloc?");
        answers.add("Group of countries with reduced trade barriers.");

        questions.add("Define foreign direct investment (FDI).");
        answers.add("Investment from one country in another's assets.");

        questions.add("Explain the infant industry argument.");
        answers.add("Protection for young domestic industries to grow.");

        questions.add("What are the implications of a devaluation of a country's currency?");
        answers.add("Cheaper exports, costlier imports, potentially better trade balance.");

        questions.add("Define trade sanctions.");
        answers.add("Penalties imposed by countries to pressure policy change.");

        questions.add("Explain the concept of trade dumping.");
        answers.add("Exporting goods below production cost for advantage.");

        questions.add("What are trade negotiations?");
        answers.add("Discussions between countries for trade agreements.");

        questions.add("Explain the concept of trade in services.");
        answers.add("Exchange of services between countries (e.g., banking, tourism).");

        questions.add("Define the terms 'exports' and 'imports'.");
        answers.add("Exports: sold abroad. Imports: bought from abroad.");

        questions.add("What is a current account deficit?");
        answers.add("Imports, transfers exceed exports and income received.");

        questions.add("Explain the concept of foreign exchange reserves.");
        answers.add("Foreign currencies held by a country's central bank.");

        internationalTrade.setQuestions(questions);
        internationalTrade.setAnswers(answers);
        internationalTrade.setCategory("Economics");
        internationalTrade.setImageResourceId(R.drawable.internationaltrade);
        flashcardList.add(internationalTrade);
    }

    public void createMonetaryPolicy(){
        Flashcard monetaryPolicy = new Flashcard();
        monetaryPolicy.setTitle("Monetary Policy");
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();

        questions.add("What is monetary policy?");
        answers.add("Central bank tools to control money supply, interest rates.");

        questions.add("Explain the role of the central bank in monetary policy.");
        answers.add("Implements policy, regulates banking system.");

        questions.add("What are the main goals of monetary policy?");
        answers.add("Stable prices, full employment, economic growth.");

        questions.add("Define inflation.");
        answers.add("Sustained increase in general price level.");

        questions.add("Explain the concept of deflation.");
        answers.add("Sustained decrease in general price level.");

        questions.add("What is the Federal Reserve (Fed)?");
        answers.add("US central bank, conducts monetary policy.");

        questions.add("Define the discount rate.");
        answers.add("Interest rate central bank lends to commercial banks.");

        questions.add("Explain open market operations.");
        answers.add("Buying, selling government securities to control money supply.");

        questions.add("What is the reserve requirement?");
        answers.add("Percentage of deposits banks must hold as reserves.");

        questions.add("Define expansionary monetary policy.");
        answers.add("Stimulates growth, increases money supply, lowers interest rates.");

        questions.add("Explain contractionary monetary policy.");
        answers.add("Slows growth, reduces money supply, raises interest rates.");

        questions.add("What is the Taylor rule?");
        answers.add("Guides interest rate setting based on inflation, economic output.");

        questions.add("Define quantitative easing.");
        answers.add("Central bank buys financial assets to increase money supply.");

        questions.add("Explain the concept of the money multiplier.");
        answers.add("Ratio of change in money supply to monetary base.");

        questions.add("What is the Phillips curve?");
        answers.add("Shows inflation-unemployment trade-off.");

        questions.add("Define the natural rate of unemployment.");
        answers.add("Unemployment rate at potential output.");

        questions.add("Explain the concept of the zero lower bound.");
        answers.add("Lower limit of near-zero interest rates.");

        questions.add("What is the role of the European Central Bank (ECB)?");
        answers.add("Eurozone's central bank, manages monetary policy, euro currency.");

        questions.add("Define exchange rate.");
        answers.add("Rate of currency exchange.");

        questions.add("Explain the concept of a currency peg.");
        answers.add("Fixed exchange rate tied to another currency or basket.");

        questions.add("What are the advantages of an independent central bank?");
        answers.add("Focus on economic stability without political interference.");

        questions.add("Define the real interest rate.");
        answers.add("Nominal interest rate adjusted for inflation.");

        questions.add("Explain the concept of forward guidance in monetary policy.");
        answers.add("Central banks use communication to influence expectations, behavior.");

        questions.add("What are the challenges of implementing effective monetary policy?");
        answers.add("Policy lags, uncertainty, unintended consequences.");

        questions.add("Define the money supply.");
        answers.add("Total money in circulation, including cash, deposits.");

        monetaryPolicy.setQuestions(questions);
        monetaryPolicy.setAnswers(answers);
        monetaryPolicy.setCategory("Economics");
        monetaryPolicy.setImageResourceId(R.drawable.monetorypolicy);
        flashcardList.add(monetaryPolicy);
    }

    public void createSupplyAndDemand(){
        Flashcard supplyAndDemand = new Flashcard();
        supplyAndDemand.setTitle("Supply and Demand");
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        questions.add("What is supply?");
        answers.add("Quantity producers offer at various price levels.");

        questions.add("What happens when there is a surplus in the market?");
        answers.add("Excess supply, price decrease until surplus is gone.");

        questions.add("What happens when there is a shortage in the market?");
        answers.add("Excess demand, price increase until shortage resolves.");

        questions.add("Explain the concept of elasticity of demand.");
        answers.add("Measures responsiveness of demand to price changes.");

        questions.add("What are the factors that influence demand?");
        answers.add("Consumer preferences, income, related goods' prices, population, advertising.");

        questions.add("What are the factors that influence supply?");
        answers.add("Input prices, technology, government policies, natural disasters.");

        questions.add("How do changes in consumer tastes and preferences affect demand?");
        answers.add("Increased preferences lead to higher demand.");

        questions.add("What is a price elasticity of demand greater than 1?");
        answers.add("Elastic demand, highly responsive to price changes.");

        questions.add("What is a price elasticity of demand less than 1?");
        answers.add("Inelastic demand, less responsive to price changes.");

        questions.add("What is the cross-price elasticity of demand?");
        answers.add("Measures how demand changes with another good's price.");

        questions.add("How does technological advancement affect supply?");
        answers.add("Increases supply, enhances efficiency, lowers costs.");

        supplyAndDemand.setQuestions(questions);
        supplyAndDemand.setAnswers(answers);
        supplyAndDemand.setCategory("Economics");
        supplyAndDemand.setImageResourceId(R.drawable.demandnsupply);
        flashcardList.add(supplyAndDemand);
    }

    public void createSgGeneralKnowledge(){
        Flashcard sgGeneralKnowledge = new Flashcard();
        sgGeneralKnowledge.setTitle("Singapore General Knowledge");
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();

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
        sgGeneralKnowledge.setImageResourceId(R.drawable.sgpropog);
        flashcardList.add(sgGeneralKnowledge);
    }

    public void createAlgebra(){
        Flashcard algebra = new Flashcard();
        algebra.setTitle("Algebra");
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();

        questions.add("Solve for x: 2x + 3 = 11");
        answers.add("x = 4");

        questions.add("Solve for x: 5x - 8 = 22");
        answers.add("x = 6");

        questions.add("Solve for x: 3(x - 2) = 15");
        answers.add("x = 7");

        questions.add("Solve for x: 4x - 9 = 23");
        answers.add("x = 8");

        questions.add("Solve for x: 2(3x - 5) = 16");
        answers.add("x = 13/3");

        questions.add("Solve for x: 2x/3 = 8");
        answers.add("x = 12");

        questions.add("Solve for x: 3x + 5 = 2x - 10");
        answers.add("x = -15");

        questions.add("Solve for x: 6(x + 4) - 3 = 27");
        answers.add("x = 1");

        questions.add("Solve for x: 2(x - 4) + 4(x + 1) = 18");
        answers.add("x = 11/3");

        questions.add("Solve for x: 4(x + 2) - 3(x - 1) = 9");
        answers.add("x = 2");

        algebra.setQuestions(questions);
        algebra.setAnswers(answers);
        algebra.setCategory("Math");
        algebra.setImageResourceId(R.drawable.algebra);
        flashcardList.add(algebra);
    }


}
