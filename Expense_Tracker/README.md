# Smart Expense Tracker
A simple Graphical User Interface (GUI) application built with Python and Tkinter to help you log, track, and analyze your daily expenses.

## Features
- **Record Daily Expenses**: Log the date, category, amount, and description for every expense.
- **Categorize Spending**: Use built-in categories such as Food, Travel, Bills, Shopping, Entertainment, and Other.
- **Monthly Summary**: View a text summary of your total expenses grouped by category for any given month.
- **Visual Insights**: Generate an interactive pie chart directly within the application to see a visual breakdown of your spending.
- **Spending Insights**: Get personalized insights suggesting the category where you spend the most, to help you cut back.
- **JSON Storage**: All data is neatly stored locally in an `expenses.json` file for easy access and backup.
- 
## Prerequisites
Before running the application, make sure you have Python installed on your system. This project requires `matplotlib` for generating the embedded pie charts.

## Installation
1. Extract the project into your desired folder.
2. Open a terminal or command prompt in that directory.
3. Install the dependencies using pip:
   ```bash
   pip install -r requirements.txt
   ```
   
## Usage
Start the graphical application by running the `gui.py` file:
```bash
python gui.py
```

### Navigating the Interface
1. **Add Expense Panel (Left Side)**: Enter the date, category, amount, and an optional description, then click "Add Expense".
2. **View Features Panel (Left Side - Bottom)**: Change the "Year/Month" string to filter the data you want to view. Click "Show Summary & Chart" to populate the right view with your data Breakdown and visual Pie Chart.
3. **Insights**: Click "Show Insights" to get simple feedback on your highest expenditure.
   
## Files Included
- `gui.py`: The main user interface application and entry point.
- `analytics.py`: Handles all data processing, including computing monthly totals, identifying insights, and generating the Matplotlib pie charts.
- `storage.py`: Handles appending and reading your data securely from the `expenses.json` file.
- `requirements.txt`: Python package requirements.
