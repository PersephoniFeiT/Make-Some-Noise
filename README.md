# Make Some Noise!
2d noise design app for digital artists and game designers

Create, customize, share, and find 2d noise texture patterns. Choose between multiple noise algorithms, customize the parameters, layer them over each other, and choose a color scheme. 

# Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

# Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

# Architecture
![image](https://github.com/user-attachments/assets/8e3c835d-9d73-4195-bb84-0a7d0f895aa4)

The software architecture is divided into a front-end, a back-end, and a server-end. The front-end is responsible for receiving user input, transmitting it to the back-end, and showing the user the back-end's output. The backend is responsible for running noise algorithms to create texture patterns, managing the user session, and communicating with the server-end. The server-end connects the rest of the application with the database and can save, load, or search for projects, as well as managing user accounts.

# Getting Started
## Application
{Download jar file?}

## Database
This program uses MySQL to save projects and user settings. 

To begin, download MySQL from https://downloads.mysql.com/archives/installer/ , run the installer, and follow the instructions. 

Launch MySQL Workbench and follow the instructions to create an account. 

Press the (+) button to create a new MySQL connection

Use the ![image](https://github.com/user-attachments/assets/81229d84-3cd4-4288-b84f-8af9dacb4ace) button to create a new schema named `makesomenoise`
Ensure that the server is running on local port 3306 (should be default).

Select this scheme and create a user called `appUser` with password `make some noise`

In the schema and add two tables: one named `accounts` and one named `projects`

Set up `accounts` like this: 

| ID  | username    | password    | email       | projectList | admin      |
|-----|-------------|-------------|-------------|-------------|------------|
| int | VARCHAR(45) | VARCHAR(45) | VARCHAR(45) | JSON        | TINYINT(1) |

![accountsTableSetup](https://github.com/user-attachments/assets/c0652894-5fd1-4622-94c2-adb005bcfd4a)


Set up `projects` like this: 

| ID  | title       | username    | dateCreated | status     | projectInfoStruct | thumbnail   | tags | accountID |
|-----|-------------|-------------|-------------|------------|-------------------|-------------|------|-----------|
| int | VARCHAR(45) | VARCHAR(45) | VARCHAR(45) | VARCHAR(7) | VARCHAR(9999)     | VARCHAR(45) | JSON | INT       |

![projectsTableSetup](https://github.com/user-attachments/assets/5fafbbd9-09b1-42e1-916d-7ddb3a7e7c92)


Save all changes, and ensure that the server is running while you use the software. You are now ready to create your noise patterns!


# Usage
Begin the software by running the .jar file. You will see an Editor with an empty canvas. 

## Editor
![image](https://github.com/user-attachments/assets/a2e47e70-333e-4cd9-833e-967c21cd28dc)


Create a noise layer by pushing the + button at the top on the right-hand side. This will add a new layer of random noise to the canvas with default parameters. You can create as many layers as you need and adjust each of their parameters to customize them

### Layer Types
This software currently has four noise algorithms: Random noise, 2D Simplex Noise, 3D Simplex Noise, and Perlin Noise. Every layer you create will use one of these algorithms to generate its noise pattern, and the editor's canvas will layer all of your layers over each other. Each of these layer types have different characteristics and give unique personality to different projects. 

### Layer Parameters
Each layer has 6 parameters: Seed, Freq (frequency), Amp (amplitude), Gain, Floor, and Ceil (ceiling). By adjusting these, you can change the behavior of the noise algorithms. 

- Seed: the seed value for random number generation. Changing this will subtly change the randomness of the algorithm
- Frequency: controls the rate of change between areas of high and low intensity
- Amplitude: determines the amount of contrast between the areas of high and low intensity
- Gain: controls the brightness of the layer
- Floor: Cut-off value for areas of low intesity; set all pixels below this value to this value
- Ceiling: Cut-off value for areas of high intesity; set all pixels above this value to this value

### Layer Mixing
Each layer can mix with the main canvas in 4 different ways: multiplication, division, addition, and subtraction. 

- Multiplication:
- Division:
- Addition:
- Subtraction: 

### Color Gradient
The color gradient lies at the bottom of the Editor. By choosing the two endpoints, you can decide what color scale is used to render the pattern. 

### Saving Projects
There are three ways to save a project. You can save the project into the database so that it can be retreived and edited more later, either by you or another user. If you don't have an account or database connection, you can save projects as a file on your own computer. Finally, you can save the project as a png image on your own computer to use the pattern in other softwares. 

## Account Panel
![image](https://github.com/user-attachments/assets/aeb89159-a491-4437-b70f-fe20e9bc46f4)


To create an account, use the drop-down menu at the top of the window to select Account -> Sign In. At the bottom of the pop-up window, select "Make an account", then fill in each field. 

To view account informatio, use the drop-down menu to select Account -> View Account. 

The top section of the Account Panel shows your username, email, and password, and allows you to change them. 

The bottom section shows the your projects that are saved in the database. By clicking one, you can open the project to edit it or delete it. 

## Sharing and Finding Panel
![image](https://github.com/user-attachments/assets/16c41723-bf8b-47fb-9683-7d649c5ba6fe)

You can search the database for other user's projects. Navigate to the search panel by going to the drop-down menu and selecting Find -> Pattern Search. You can look at patterns saved to the database by searching for matching tags, or titles. After searching, select a project to open a copy of it in an editor window. 


