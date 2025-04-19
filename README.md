# Make-Some-Noise
2d noise design app for digital artists and game designers

Create, customize, share, and find 2d noise texture patterns. Choose between multiple noise algorithms, customize the parameters, layer them over each other, and choose a color scheme. 

# Architecture
{Add image}

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

Select this schema and add two tables; one named `accounts` and one named `projects`

Set up `accounts` like this: ![image](https://github.com/user-attachments/assets/4bc89455-d91d-4572-9432-8162c7e7a8c0)

Set up `projects` like this: ![image](https://github.com/user-attachments/assets/f4a52105-5b21-4056-a21a-c499fb810947)

Save all changes, and ensure that the server is running while you use the software. You are now ready to create your noise patterns!

# Usage
Begin the software by running the .jar file. You will see an Editor with an empty canvas. 

## Editor
{Add image}

Create a noise layer by pushing the + button at the top on the right-hand side. This will add a new layer of random noise to the canvas with default parameters. You can create as many layers as you need and adjust each of their parameters to customize them

### Layer Types
This software currently has four noise algorithms: Random noise, 2D Simplex Noise, 3D Simplex Noise, and Perlin Noise. Every layer you create will use one of these algorithms to generate its noise pattern, and the editor's canvas will layer all of your layers over each other. Each of these layer types have different characteristics and give unique personality to different projects. 

### Layer Parameters
Each layer has 6 parameters: Seed, Freq (frequency), Amp (amplitude), Gain, Floor, and Ceil (ceiling). By adjusting these, you can change the behavior of the noise algorithms. 

- Seed: {}
- Frequency: {}
- Amplitude: {}
- Gain: {}
- Floor: {}
- Ceiling: {}

### Color Gradient
The color gradient lies at the bottom of the Editor. By choosing the two endpoints, you can decide what color scale is used to render the pattern. 

### Saving Projects
There are three ways to save a project. You can save the project into the database so that it can be retreived and edited more later, either by you or another user. If you don't have an account or database connection, you can save projects as a file on your own computer. Finally, you can save the project as a png image on your own computer to use the pattern in other softwares. 

## Account Panel
{Add image}






## Sharing and Finding Panel
{Add image}







