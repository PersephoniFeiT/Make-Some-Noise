# Make-Some-Noise
2d noise design app for digital artists and game designers

Create, customize, share, and find 2d noise texture patterns. Choose between multiple noise algorithms, customize the parameters, layer them over each other, and choose a color scheme. 

# Architecture



# Getting Started
## Applcication
{Download jar file?}

## Database
This program uses MySQL to save projects and user settings. 

To begin, download MySQL from https://downloads.mysql.com/archives/installer/ , run the installer, and follow the instructions. 

Launch MySQL Workbench and follow the instructions to create an account. 

Press the (+) button to create a new MySQL connection

Use the ![image](https://github.com/user-attachments/assets/81229d84-3cd4-4288-b84f-8af9dacb4ace) to create a new schema named `makesomenoise`

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
This software currently has four noise algorithms: Random noise, 2D Simplex Noise, 3D Simplex Noise, and Perlin Noise. Every layer you create will use one of these algorithms to generate its noise pattern. Each of these layer types have different characteristics and give unique personality to different works. 

### Parameters


### Color 


### Saving


## Account Panel


## Sharing and Finding Panel
