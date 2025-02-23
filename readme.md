# 🎯 Overview
Solution to the roadmap.sh project https://roadmap.sh/projects/expense-tracker

## 🔍 Features
- ✅ Add an expense with a description and amount
- ✅ Update an existing expense
- ✅ Delete an expense
- ✅ View all expenses
- ✅ View a summary of total expenses
- ✅ View a summary of expenses for a specific month
- ✅ Filter by category for expenses
- ✅ Set a monthly budget and show a warning when the budget is exceeded
- ✅ Export expenses to a CSV file

## 💾 Data Source:

Expenses are stored in a simple text file and data is saved in JSON format.

## 🛠️ Technical Details: This project is built using:

This project is an easy to use and lightweight command line interface (CLI) application.

- Java 19
- Maven for build management (version 3.9.9)
- Jackson for JSON processing
- GitHub API for retrieving user activities

## 🏃 How to Run: To get started with the project, follow these steps:

- Clone the repository:
        
        git clone https://github.com/your-username/expense-tracker.git


- Navigate to the project directory:

        cd expense-tracker

- Build the project:

        mvn clean install

Run the application:

        mvn exec:java

## 📘 Usage Example: 

After the application runs, enter the “help” command and the project will help you.
