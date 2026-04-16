import json
import os
from datetime import datetime

STORAGE_FILE = 'expenses.json'

def load_expenses():
    if not os.path.exists(STORAGE_FILE):
        return []
    try:
        with open(STORAGE_FILE, 'r') as f:
            return json.load(f)
    except json.JSONDecodeError:
        return []

def save_expenses(expenses):
    with open(STORAGE_FILE, 'w') as f:
        json.dump(expenses, f, indent=4)

def add_expense(date, category, amount, description):
    expenses = load_expenses()
    expenses.append({
        'id': len(expenses) + 1,
        'date': date,
        'category': category,
        'amount': float(amount),
        'description': description
    })
    save_expenses(expenses)
