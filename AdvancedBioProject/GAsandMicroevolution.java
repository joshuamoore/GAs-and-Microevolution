import java.util.Random;
//Program:		GAsandMicroevolution.java
//Course:		COSC407
//Description:	Uses microevlution via a genetic algorithm to simulate a hypothetical environment
//Author:		Josh Moore
//Revised:		5/8/10
//Compiler:		Visual J#
//Comments:		None
//Notes:		None
public class GAsandMicroevolution
{
	static TextFileClass preyText = new TextFileClass(0);
	static TextFileClass predatorText = new TextFileClass(0);
	static KeyboardInputClass keyboardInput = new KeyboardInputClass();
	static LList organisims = new LList();
	static ImageConstruction graphicsBoard;
	static Random generateRandom = new Random();

	//Method:		main()
	//Description:	Calls menu()
	//Parameters:  	None
	//Returns:     	None
	//Calls:       	None
	//Globals:		None
	public static void main(String[] args)
	{
		menu();
	}
	//********************************************************************

	//Method:		menu()
	//Description:	Displays menu, allows user to specify variables, initialized ImageConstruction class, populates linked list
	//Parameters:  	option, userInput, horizontalPixels, verticalPixels, decFood, numFood, graphicsBoard, preyNum, predatorNum,
	//				preyBinary, predatorBinary, xValue, yValue, intWeight
	//Returns:     	None
	//Calls:       	ImageContstruction(), OrganismObject(), addFood(), iterateGenerations()
	//Globals:		preyText, predatorText, keyboardInput, organisms
	public static void menu()
	{
	int option = 5;
		while (option != 0)
		{
			System.out.println("Microevolution and Genetic Algorithms");
			System.out.println("\n1. Generate organism codes randomly");
			System.out.println("2. Enter codes from text file");
			System.out.println("0. Exit the program\n");

			String userInput = "";
			userInput = keyboardInput.getKeyboardInput("Please choose a menu item");
			option = Integer.parseInt(userInput);

			if (option == 0)
			{
				graphicsBoard.closeDisplay();
				System.exit(0);
			}

			userInput = keyboardInput.getKeyboardInput("\nHow many horizontal pixels will there be in the output? (default 500px)");
			if (userInput.equals(""))
				userInput = "500";
			int horizontalPixels = Integer.parseInt(userInput);
			userInput = keyboardInput.getKeyboardInput("\nHow many vertical pixels will there be in the output? (default 250px)");
			if (userInput.equals(""))
				userInput = "250";
			int verticalPixels = Integer.parseInt(userInput);

			graphicsBoard = new ImageConstruction(verticalPixels, horizontalPixels, (double)0, (double)(horizontalPixels - 1), (double)0, (double)(verticalPixels - 1), 1);
			graphicsBoard.displaySetup(); //creates GUI
			graphicsBoard.displayImage(true, "GAs and Microevolution", false);

			userInput = keyboardInput.getKeyboardInput("\nHow many food particles will be added to the board?");
			int numFood = Integer.parseInt(userInput);

			int preyNum, predatorNum;

			switch (option)
			{
				case 2:
					preyText.getFileName("\nSpecify the name of the text file that contains prey values");
					predatorText.getFileName("\nSpecify the name of the text file that contains predator values");
					if ((preyText.fileName.length() > 0) && (predatorText.fileName.length() > 0))
					{
						preyText.getFileContents();
						preyNum = preyText.lineCount;
						OrganismObject newOrganism;
						for (int r = 0; r < preyText.lineCount; r++)
						{
							String preyBinary = preyText.text[r];
							int xValue = randomNumber(verticalPixels);
							int yValue = randomNumber(horizontalPixels);
							int intWeight = randomNumber(15) + 10;
							newOrganism = new OrganismObject();
							newOrganism.add("prey", xValue, yValue, preyBinary, intWeight, 100, 0);
							newOrganism.health = 100;
							organisims.add(newOrganism);
						}

						predatorText.getFileContents();
						predatorNum = predatorText.lineCount;
						for (int r = 0; r < predatorText.lineCount; r++)
						{
							String predatorBinary = predatorText.text[r];
							int xValue = randomNumber(verticalPixels);
							int yValue = randomNumber(horizontalPixels);
							int intWeight = randomNumber(15) + 10;
							newOrganism = new OrganismObject();
							newOrganism.add("predator", xValue, yValue, predatorBinary, intWeight, 100, 0);
							newOrganism.health = 100;
							organisims.add(newOrganism);
						}

						organisims = addFood(organisims, horizontalPixels, verticalPixels, numFood);
						System.out.println();
						iterateGenerations(organisims, verticalPixels, horizontalPixels, numFood);
					}
					break;

				default:
					userInput = keyboardInput.getKeyboardInput("\nHow many prey should be added to the population?");
					preyNum = Integer.parseInt(userInput); ;

					userInput = keyboardInput.getKeyboardInput("\nHow many predators should be added to the population?");
					predatorNum = Integer.parseInt(userInput);

					String preyString;
					String predatorString;
					int preyInt;
					int predatorInt;
					OrganismObject newOrganism;

					for (int i = 0; i < preyNum; i++)
					{
						String randomString = Integer.toString(32768 + randomNumber(32767));
						int randomInt = Integer.parseInt(randomString);
						String preyBinary = Integer.toBinaryString(randomInt);
						int xValue = randomNumber(horizontalPixels);
						int yValue = randomNumber(verticalPixels);
						int intWeight = randomNumber(15) + 10;
						newOrganism = new OrganismObject();
						newOrganism.add("prey", xValue, yValue, preyBinary, intWeight, 20, 0);
						newOrganism.health = 100;
						organisims.add(newOrganism);
					}

					for (int j = 0; j < predatorNum; j++)
					{
						String randomString = Integer.toString(32768 + randomNumber(32767));
						int randomInt = Integer.parseInt(randomString);
						String predatorBinary = Integer.toBinaryString(randomInt);
						int xValue = randomNumber(horizontalPixels);
						int yValue = randomNumber(verticalPixels);
						int intWeight = randomNumber(15) + 10;
						newOrganism = new OrganismObject();
						newOrganism.add("predator", xValue, yValue, predatorBinary, intWeight, 20, 0);
						newOrganism.health = 100;
						organisims.add(newOrganism);
					}

					organisims = addFood(organisims, horizontalPixels, verticalPixels, numFood);
					iterateGenerations(organisims, verticalPixels, horizontalPixels, numFood);
					break;
			}
		}
	}
	//********************************************************************

	//Method:		iterateGenerations()
	//Description:	Simulates generations and passes off linked list to be manipulated
	//Parameters:  	userInput, generations, animal, preyThreshold, predatorThreshold, generations, x, y, animalAge, animalFood
	//Returns:     	Organimsms
	//Calls:       	preyAteFood(), predatorAtePrey(), evaluateOrganisms(), countFood(), graphicsBoard, display(), iterateGenerations()
	//Globals:		preyText, predatorText, keyboardInput, organisms
	public static void iterateGenerations(LList organisms, int verticalPixels, int horizontalPixels, int numFood)
	{
		String userInput = keyboardInput.getKeyboardInput("\nHow many time steps should be simulated? (0 to exit, default 25)");
		if (userInput.equals(""))
			userInput = "25";
		int generations = Integer.parseInt(userInput);
		if (generations == 0)
		{
			graphicsBoard.closeDisplay();
			menu();
		}
		OrganismObject animal;

		userInput = keyboardInput.getKeyboardInput("\nWhat is the mutation decimal number threshold for prey (0 to 1, .9 default)?");
		if (userInput.equals(""))
			userInput = ".9";
		double preyThreshold = Double.parseDouble(userInput);

		userInput = keyboardInput.getKeyboardInput("\nWhat is the mutation decimal number threshold for predator (0 to 1, .8 default)?");
		if (userInput.equals(""))
			userInput = ".8";
		double predatorThreshold = Double.parseDouble(userInput);
		System.out.println();

		for (int i = 0; i < generations; i++)
		{
				for (int j = 1; j <= organisms.getLength(); j++)
				{
					animal = (OrganismObject)organisms.getEntry(j);
					String name = animal.name;

					if (name == "prey")
					{
						animal.genome = mutatePrey(animal, animal.genome, animal.age, preyThreshold);

						int x = animal.x;
						int y = animal.y;
						int animalAge = animal.age;
						animalAge++;
						animal.age = animalAge;
						int animalFood = animal.food;
						animalFood -= 1;
						animal.food = animalFood;
						graphicsBoard.insertBox((double)(x - 5), (double)(y - 5), (double)(x + 5), (double)(y + 5), 0, 255, 255, true);

						animal = movePrey(animal, verticalPixels, horizontalPixels, organisims);

						organisms.replace(j, animal);
					}

					if (name == "predator")
					{
						animal.genome = mutatePredator(animal, animal.genome, animal.age, predatorThreshold);

						int x = animal.x;
						int y = animal.y;
						int animalAge = animal.age;
						animalAge++;
						animal.age = animalAge;
						int animalFood = animal.food;
						animalFood -= 1;
						animal.food = animalFood;
						//graphicsBoard.blueValues[x][y] = 255;
						//for (int r = y; r <= y; r++)
						//{
						//    for (int c = x; c <= x; c++)
						//    {
						//        graphicsBoard.redValues[r][c] = 200;
						//        graphicsBoard.greenValues[r][c] = 0;
						//        graphicsBoard.blueValues[r][c] = 200;
						//    }
						//}
						graphicsBoard.insertBox((double)(x - 5), (double)(y - 5), (double)(x + 5), (double)(y + 5), 255, 0, 255, true);

						animal = movePredator(animal, verticalPixels, horizontalPixels, organisms);

						organisms.replace(j, animal);
					}

					if (name == "food")
					{
						int x = animal.x;
						int y = animal.y;
						graphicsBoard.insertBox((double)(x - 5), (double)(y - 5), (double)(x + 5), (double)(y + 5), 0, 255, 0, true);
					}
				}
				organisms = preyAteFood(organisms, verticalPixels, horizontalPixels);  //checks to see if prey is in same position as food
				organisms = predatorAtePrey(organisms, verticalPixels, horizontalPixels);  //checks to see if predator is in same position as prey
				organisims = evaluateOrganisms(organisims);  //checks the health value (0-100) of prey and predator
				organisims = addFood(organisims, horizontalPixels, verticalPixels, numFood);  //adds food back to linked list in random places
				if (i != (generations - 1))
				{
					graphicsBoard.clearImage(0, 0, 0);  //clears image from image class
					graphicsBoard.setPixelValues();  //puts images back onto image class
				}
		}

		countList(organisms, generations);
		userInput = (keyboardInput.getKeyboardInput("\nWould you like to see the resulting mutated population? (y or n)")).toUpperCase();
		if (userInput.equals("Y"))
			display(organisims);
		iterateGenerations(organisims, verticalPixels, horizontalPixels, numFood);
		System.out.println();
	}
	//********************************************************************

	//Method:		randomNumber()
	//Description:	generates a random number between 1 and length
	//Parameters:  	random
	//Returns:     	random
	//Calls:       	None
	//Globals:		None
	public static int randomNumber(int length)
	{
		if (length == 0)
			length = 1;

		int random = generateRandom.nextInt(length);

		return random;
	}
	//********************************************************************

	//Method:		mutatePrey()
	//Description:	Mutates the prey's genome if a randomly generated double is higher than the mutation threshold
	//Parameters:  	crossoverPosPrey, mutatePrey, onePrey, leftOne, rightOne, strOnePrey, leftTwo, posTwo, rightTwo, twoPrey, strTwoPrey
	//Returns:     	genome
	//Calls:       	randomNumber()
	//Globals:		None
	public static String mutatePrey(OrganismObject animal, String genome, int age, double preyThreshold)
	{
		int crossoverPosPrey;
		double mutatePrey;

		if (animal.canReproduce())
		{

			mutatePrey = (double)(randomNumber(9) + 1) / 10;

			String onePrey = genome;
			if (mutatePrey > preyThreshold)
			{
				//System.out.println("crossover");
				crossoverPosPrey = randomNumber(15);
				if (crossoverPosPrey == 0)
					crossoverPosPrey = 2;
				if (crossoverPosPrey == 15)
					crossoverPosPrey = 13;

				String leftOne = onePrey.substring(0, (crossoverPosPrey));
				String rightOne = onePrey.substring(crossoverPosPrey, 16);

				String strOnePrey = rightOne + leftOne;
				onePrey = strOnePrey;
			}

			mutatePrey = (double)(randomNumber(9) + 1) / 10;

			String leftTwo, posTwo, rightTwo;
			String twoPrey = onePrey;
			if (mutatePrey > preyThreshold)
			{
				//System.out.println("mutate");
				crossoverPosPrey = randomNumber(15);

				if (crossoverPosPrey < 2)
					crossoverPosPrey = 2;
				if (crossoverPosPrey > 13)
					crossoverPosPrey = 13;

				leftTwo = twoPrey.substring(0, (crossoverPosPrey));
				posTwo = twoPrey.substring(crossoverPosPrey, crossoverPosPrey + 1);
				rightTwo = twoPrey.substring((crossoverPosPrey + 1), 16);

				if (posTwo.equals("0"))
					posTwo = "1";
				if (posTwo.equals("1"))
					posTwo = "0";

				String strTwoPrey = leftTwo + posTwo + rightTwo;
				twoPrey = strTwoPrey;
				genome = twoPrey;
			}
		}
		return genome;
	}
	//********************************************************************

	//Method:		mutatePredator()
	//Description:	Mutates the predator's genome if a randomly generated double is higher than the mutation threshold
	//Parameters:  	crossoverPosPredator, mutatePredator, onePredator, leftOne, rightOne, strOnePredator, 
	//				leftTwo, posTwo, rightTwo, twoPredator, strTwoPredator
	//Returns:     	genome
	//Calls:       	randomNumber()
	//Globals:		None
	public static String mutatePredator(OrganismObject animal, String genome, int age, double predatorThreshold)
	{
		int crossoverPosPredator;
		double mutatePredator;

		if (animal.canReproduce())
		{

			mutatePredator = (double)(randomNumber(9) + 1) / 10;

			String onePred = genome;
			if (mutatePredator > predatorThreshold)
			{
				crossoverPosPredator = randomNumber(15);
				if (crossoverPosPredator == 0)
					crossoverPosPredator = 2;
				if (crossoverPosPredator == 15)
					crossoverPosPredator = 13;

				String leftOne = onePred.substring(0, crossoverPosPredator);
				String rightOne = onePred.substring(crossoverPosPredator, 16);

				String strOnePredator = rightOne + leftOne;
				onePred = strOnePredator;
			}

			mutatePredator = (double)(randomNumber(9) + 1) / 10;

			String leftTwo, posTwo, rightTwo;
			String twoPred = onePred;
			if (mutatePredator > predatorThreshold)
			{
				//System.out.println("mutate");
				crossoverPosPredator = randomNumber(15);

				if (crossoverPosPredator < 2)
					crossoverPosPredator = 2;
				if (crossoverPosPredator > 13)
					crossoverPosPredator = 13;

				leftTwo = twoPred.substring(0, (crossoverPosPredator));
				posTwo = twoPred.substring(crossoverPosPredator, crossoverPosPredator + 1);
				rightTwo = twoPred.substring((crossoverPosPredator + 1), 16);

				if (posTwo.equals("0"))
					posTwo = "1";
				if (posTwo.equals("1"))
					posTwo = "0";

				String strTwoPredator = leftTwo + posTwo + rightTwo;
				twoPred = strTwoPredator;
				genome = twoPred;
			}
		}
		return genome;
	}
	//********************************************************************

	//Method:		addFood()
	//Description:	Replenishes food that has been eaten in the last time step
	//Parameters:  	newOrganisms, foodRemaining, xValue, yValue
	//Returns:     	None
	//Calls:       	getEntry(), randomNumber(), add()
	//Globals:		None
	public static LList addFood(LList organisms, int horizontalPixels, int verticalPixels, int numFood)
	{
		OrganismObject newOrganism;
		int foodRemaining = 0;

		for (int i = 1; i < organisims.getLength(); i++)
		{
			newOrganism = (OrganismObject)organisms.getEntry(i);

			if (newOrganism.name.equals("food"))
				foodRemaining++;
		}

		numFood -= foodRemaining;

		for (int k = 0; k < numFood; k++)
		{
			int xValue = randomNumber(horizontalPixels);
			int yValue = randomNumber(verticalPixels);
			newOrganism = new OrganismObject();
			newOrganism.add("food", xValue, yValue);
			organisims.add(newOrganism);
		}
		return organisims;
	}
	//********************************************************************

	//Method:		preyAteFood()
	//Description:	Checks to see if the x and y values for a preyare the same as a food's position, if so then 
	//				the food is removed and the prey's food count is incremented.
	//Parameters:  	animal, search, xAnimal, yAnimal, xSearch, ySearch
	//Returns:     	organisms
	//Calls:       	getEntry(), getLength(), replace(), remove()
	//Globals:		None
	public static LList preyAteFood(LList organisms, int verticalPixels, int horizontalPixels)
	{
		OrganismObject animal;
		OrganismObject search;
		int xAnimal;
		int yAnimal;
		int xSearch;
		int ySearch;
		for (int i = 1; i <= organisms.getLength(); i++)
		{
			animal = (OrganismObject)organisms.getEntry(i);
			xAnimal = animal.x;
			yAnimal = animal.y;

			for (int j = 1; j <= organisms.getLength(); j++)
			{
				search = (OrganismObject)organisms.getEntry(j);
				xSearch = search.x;
				ySearch = search.y;

				if ((animal.name.equals("prey")) && (search.name.equals("food")))
				{
					if ((animal.x == search.x) && (animal.y == search.y))
					{
						System.out.println("prey ate food");
						animal.food += 100;
						organisms.replace(i, animal);
						organisms.remove(j);
					}
				}
			}
		}

		return organisms;
	}
	//********************************************************************

	//Method:		predatorAtePrey()
	//Description:	Checks to see if the x and y values for a prey are the same as a predator's position, if so then the 
	//				food is removed and the prey's food count is incremented.
	//Parameters:  	animal, search, xAnimal, yAnimal, xSearch, ySearch
	//Returns:     	organisms
	//Calls:       	getEntry(), getLength(), replace(), remove()
	//Globals:		None
	public static LList predatorAtePrey(LList organisms, int verticalPixels, int horizontalPixels)
	{
		OrganismObject predator;
		OrganismObject prey;
		int xPredator, yPredator, xPrey, yPrey;
		for (int i = 1; i <= organisms.getLength(); i++)
		{
			predator = (OrganismObject)organisms.getEntry(i);
			xPredator = predator.x;
			yPredator = predator.y;

			for (int j = 1; j <= organisms.getLength(); j++)
			{
				prey = (OrganismObject)organisms.getEntry(j);
				xPrey = prey.x;
				yPrey = prey.y;

				if ((predator.name.equals("predator")) && (prey.name.equals("prey")))
				{
					if ((predator.x == prey.x) && (predator.y == prey.y))
					{
						System.out.println("predator ate prey");
						predator.food += 150;
						organisms.replace(i, predator);
						organisms.remove(j);
					}
				}
			}
		}

		return organisms;
	}
	//********************************************************************

	//Method:		evaluateOrganisms()
	//Description:	Evaluates organims' food, health, weight, and metabolism levels
	//Parameters:  	animal, weight
	//Returns:     	organisms
	//Calls:       	getEntry(), computeHealth(), substring(), remove()
	//Globals:		None
	public static LList evaluateOrganisms(LList organisms)
	{
		OrganismObject animal;

		for (int i = 1; i <= organisms.getLength(); i++)
		{
			animal = (OrganismObject)organisms.getEntry(i);
			//System.out.println("animal food: " + animal.food);
			//System.out.println("animal weight: " + animal.weight);
			if (animal.name.equals("prey"))
			{
				animal.health = animal.computeHealth();
				if (animal.food > 250)
				{
					int weight = animal.weight;
					weight++;
					animal.weight = weight;
				}
				//System.out.println("prey health: " + (int)animal.health);

				int metabolism = Integer.parseInt((animal.genome.substring(12, 16)), 2);
				if (metabolism == 0)
				{
					organisims.remove(i);
					System.out.println("prey died, metabolism 0");
				}
				if (animal.food < 0)
				{
					organisims.remove(i);
					System.out.println("prey died, no remaining food stores");
				}
				if ((int)animal.health <= 0)
				{
					organisims.remove(i);
					System.out.println("prey died, health 0%");
				}
			}

			if (animal.name.equals("predator"))
			{
				animal.health = animal.computeHealth();
				if (animal.food > 250)
				{
					int weight = animal.weight;
					weight++;
					animal.weight = weight;
				}
				//System.out.println("predator health: " + (int)animal.health);

				int metabolism = Integer.parseInt((animal.genome.substring(12, 16)), 2);
				if (metabolism == 0)
				{
					organisims.remove(i);
					System.out.println("predator died, metabolism 0");
				}
				if (animal.food < 0)
				{
					organisims.remove(i);
					System.out.println("predator died, no remaining food stores");
				}
				if ((int)animal.health <= 0)
				{
					organisims.remove(i);
					System.out.println("predator died, health 0%");
				}
			}
		}

		return organisms;
	}
	//********************************************************************

	//Method:		movePrey()
	//Description:	Moves prey around board which would be to eat food and avoid predators
	//Parameters:  	sightString, speedString, sightInt, speedInt, xAnimal, yAnimal, xMove,
	//				bestX, lowestXValue, bestY, lowestYValue, xCompare, yCompare, xPredator, yPredator
	//Returns:     	OrganismObject
	//Calls:       	substring(), parseInt(), randomNumber(), getLength(), Math.abs()
	//Globals:		randomNumber()
	public static OrganismObject movePrey(OrganismObject animal, int verticalPixels, int horizontalPixels, LList organisms)
	{
		OrganismObject compare;
		String sightString = animal.genome.substring(0, 4);
		String speedString = animal.genome.substring(4, 8);
		int sightInt = Integer.parseInt(sightString, 2);
		int speedInt = Integer.parseInt(speedString, 2);

		int xAnimal = animal.x;
		int yAnimal = animal.y;

		int xMove = randomNumber((int)(speedInt));
		xMove += randomNumber((int)(sightInt));
		int yMove = randomNumber((int)(speedInt));
		yMove += randomNumber((int)(sightInt));

		int bestX = horizontalPixels;
		int lowestXValue = horizontalPixels;
		int bestY = verticalPixels;
		int lowestYValue = verticalPixels;
		for (int i = 1; i <= organisims.getLength(); i++)
		{
			compare = (OrganismObject)organisims.getEntry(i);
			if (compare.name.equals("food"))
			{

				int xCompare = compare.x;
				int yCompare = compare.y;
				if (((int)Math.abs(xAnimal - xCompare)) < bestX)
				{
					bestX = xCompare;
					lowestXValue = (int)Math.abs(xAnimal - xCompare);
				}
				if (((int)Math.abs(yAnimal - yCompare)) < bestY)
				{
					bestY = yCompare;
					lowestYValue = (int)Math.abs(yAnimal - yCompare);
				}
			}
		}
		//System.out.println("best prey location: " + bestX + "," + bestY);
		//System.out.println("lowest prey move: " + lowestXValue + "," + lowestYValue);

		if (bestX > xAnimal)
		{
			xAnimal += xMove;
			if (xAnimal > bestX)
				xAnimal = bestX;
		}
		else
		{
			xAnimal -= xMove;
			if (xAnimal < bestX)
				xAnimal = bestX;
		}
		if (xAnimal < 0)
			xAnimal = horizontalPixels - xMove;
		if (xAnimal > (horizontalPixels - 1))
			xAnimal = xMove;

		if (bestY > yAnimal)
		{
			yAnimal += yMove;
			if (yAnimal > bestY)
				yAnimal = bestY;
		}
		else
		{
			yAnimal -= yMove;
			if (yAnimal < bestY)
				yAnimal = bestY;
		}
		if (yAnimal < 0)
			yAnimal = verticalPixels - yMove;
		if (yAnimal > (verticalPixels - 1))
			yAnimal = yMove;

		int xPredator = 0;
		int yPredator = 0;
		for (int j = 1; j <= organisims.getLength(); j++)
		{
			compare = (OrganismObject)organisims.getEntry(j);
			if (compare.name.equals("predator"))
			{
				xPredator = compare.x;
				yPredator = compare.y;
				if (((int)Math.abs(xAnimal - xPredator)) < 5)
					xAnimal -= 5;
				if (((int)Math.abs(yAnimal - yPredator)) < 5)
					yAnimal -= 5;
			}
		}

		animal.x = xAnimal;
		animal.y = yAnimal;

		return animal;
	}
	//********************************************************************

	//Method:		movePredator()
	//Description:	Moves predator around board which would be to eat food and avoid predators
	//Parameters:  	sightString, speedString, sightInt, speedInt, xAnimal, yAnimal, xMove,
	//				bestX, lowestXValue, bestY, lowestYValue, xCompare, yCompare
	//Returns:     	OrganismObject
	//Calls:       	substring(), parseInt(), randomNumber(), getLength(), Math.abs()
	//Globals:		randomNumber()
	public static OrganismObject movePredator(OrganismObject animal, int verticalPixels, int horizontalPixels, LList organisms)
	{
		OrganismObject compare;
		String sightString = animal.genome.substring(0, 4);
		String speedString = animal.genome.substring(4, 8);
		int sightInt = Integer.parseInt(sightString, 2);
		int speedInt = Integer.parseInt(speedString, 2);

		int xAnimal = animal.x;
		int yAnimal = animal.y;

		int xMove = randomNumber((int)(speedInt));
		xMove += randomNumber((int)(sightInt));
		int yMove = randomNumber((int)(speedInt));
		yMove += randomNumber((int)(sightInt));

		int bestX = horizontalPixels;
		int lowestXValue = horizontalPixels;
		int bestY = verticalPixels;
		int lowestYValue = verticalPixels;
		for (int i = 1; i <= organisims.getLength(); i++)
		{
			compare = (OrganismObject)organisims.getEntry(i);
			if (compare.name.equals("prey"))
			{

				int xCompare = compare.x;
				int yCompare = compare.y;
				if (((int)Math.abs(xAnimal - xCompare)) < bestX)
				{
					bestX = xCompare;
					lowestXValue = (int)Math.abs(xAnimal - xCompare);
				}
				if (((int)Math.abs(yAnimal - yCompare)) < bestY)
				{
					bestY = yCompare;
					lowestYValue = (int)Math.abs(yAnimal - yCompare);
				}
			}
		}
		//System.out.println("best predator location: " + bestX + "," + bestY);
		//System.out.println("predator move: " + lowestXValue + "," + lowestYValue);

		if (bestX > xAnimal)
		{
			xAnimal += xMove;
			if (xAnimal > bestX)
				xAnimal = bestX;
		}
		else
		{
			xAnimal -= xMove;
			if (xAnimal < bestX)
				xAnimal = bestX;
		}
		if (xAnimal < 0)
			xAnimal = horizontalPixels - xMove;
		if (xAnimal > (horizontalPixels - 1))
			xAnimal = xMove;


		if (bestY > yAnimal)
		{
			yAnimal += yMove;
			if (yAnimal > bestY)
				yAnimal = bestY;
		}
		else
		{
			yAnimal -= yMove;
			if (yAnimal < bestY)
				yAnimal = bestY;
		}
		if (yAnimal < 0)
			yAnimal = verticalPixels - yMove;
		if (yAnimal > (verticalPixels - 1))
			yAnimal = yMove;

		animal.x = xAnimal;
		animal.y = yAnimal;

		return animal;
	}
	//********************************************************************

	//Method:		countList()
	//Description:	Outputs how many prey and predators survived over the user-specified
	//				amount of time steps
	//Parameters:  	organisms, generations
	//Returns:     	None
	//Calls:       	getLength(), getEntry(), equals()
	//Globals:		None
	public static void countList(LList organisms, int generations)
	{
		int preyCount = 0;
		int predatorCount = 0;
		OrganismObject animal;

		for (int i = 1; i < organisims.getLength(); i++)
		{
			animal = (OrganismObject)organisims.getEntry(i);

			if (animal.name.equals("prey"))
				preyCount++;
			if (animal.name.equals("predator"))
				predatorCount++;
		}
		System.out.println("\n" + preyCount + " prey and " + predatorCount + " predators survived over " + generations + " time steps.");
	}
	//********************************************************************

	//Method:		display()
	//Description:	Prints out the contents of the linked list (organisms)
	//Parameters:  	list
	//Returns:     	None
	//Calls:       	getEntry()
	//Globals:		None
	public static void display(LList list)
	{
		OrganismObject animal;

		for (int i = 1; i <= list.getLength(); i++)
		{
			animal = (OrganismObject)list.getEntry(i);
			String name = animal.name;
			String genome = animal.genome;
			int x = animal.x;
			int y = animal.y;

			if (name != "food")
				System.out.println(name + " genome: " + genome + " position: " + x + "," + y + "\thealth: " + (int)animal.health + "%\tfood: " + animal.food);
		}
	}
	//********************************************************************
}
//*******************************************************************************
//*******************************************************************************