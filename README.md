# Shadow Adventure

A **2D** action **RPG game** developed in Java **(swing)** using Object-Oriented Programming (OOP) principles. Players engage in a ***1v1*** battle against a mage, with unique abilities and animations, followed by a boss fight against the Skeleton King to progress into a cave using ***AI logic***


-
## 1. Overview
Shadow Adventure puts players in a **1v1 battle** where you control the ***Warrior*** character, facing off against a **mage controlled by the AI**. Both characters have **unique abilities**, which come with animations that were custom-designed. The game features a **collision logic**, as well as **HP**, **mana**, and **aggro mechanics**, adding depth to the combat system. After defeating the mage, the player proceeds to fight the ***Skeleton King***, a monster guarding the entrance to a cave. If the player defeats the king, they win the game. Additionally, the game features where the player can collect a mana boost from a **randomly generated position on the map**. The map is handcrafted, created entirely by me using pixel tiles in Swing

## 2. Main Features
### a). Character Battle
* **Warrior vs. Mage**: Engage in a 1v1 battle where you control the Warrior and face an AI-controlled Mage.
* **Abilities**: Both characters have unique abilities:
  * **Warrior**: 
  
  *'Earthshatter'* - Hits the ground and damages all nearby enemies.
  * **Mage**: 
  
  *'Pyroclasm'* -  Releases an explosion of fire in all directions, damaging player when nearby by 20%



* **HP and Mana**: Each character has their own health (HP) and mana (MP) bars that are managed during the fight.

* **Collision Logic**: Collisions are handled for both attacks and movements.

### b). Boss Fight: Skeleton King
After defeating the Mage, you face the Skeleton King, a boss character guarding the entrance to a castle.
*The Skeleton King uses **aggro logic**, meaning **it targets the player and reacts to their movements and attacks**.*
Defeating the Skeleton King grants access to the castle, marking the game's victory condition.

### c). Resource Management
* **Mana**: Used to perform special abilities. Each character has a mana bar that decreases with ability use and get recharged.with random postion on the map
* **HP**: The health of both the player and monster decreases with damage taken.

### d). Aggro and AI Logic
The Mage and Skeleton King both have AI-controlled behavior.
The Mage uses abilities to challenge the player in combat, while the Skeleton King targets the player with aggro logic during the boss fight.
### e). Dynamic Combat Mechanics
Combat involves managing both your character’s HP and Mana, and responding strategically to the Mage's ability and attacks and the Skeleton King's attacks.

## 3. Technologies Used
### a). Language
* **Java** : The primary language for developing the game logic, character interactions, animations, and AI.
### b). User Interface (UI)
* **Java Swing**: A simple graphical interface where the player sees their character, the enemy, health/mana bars, mana posion, ability animation, and the game environment.
### c). Object-Oriented Programming (OOP)
* **Classes**: 'Warrior', 'Mage', 'SkeletonKing', 'Player', 'Game', 'Map', etc.
* **Inheritance**: Common functionality shared between characters (e.g., base class for characters).
* **Encapsulation**: Getters and setters for character attributes such as health and mana.
* **Polymorphism**: Character abilities and actions are polymorphic, overriding common methods from base classes. e.g 'run'

## 4. Contributors 
* AMERYAHIA Mélissa https://github.com/mmellyo 


