import matplotlib.pyplot as plt
from matplotlib.figure import Figure
from collections import defaultdict
from storage import load_expenses

def get_monthly_summary(year, month):
    expenses = load_expenses()
    summary = defaultdict(float)
    total = 0.0

    target_prefix = f"{year}-{month:02d}"

    for expense in expenses:
        if expense['date'].startswith(target_prefix):
            amount = expense['amount']
            summary[expense['category']] += amount
            total += amount

    return dict(summary), total

def get_insights(summary):
    if not summary:
        return None

    highest_category = max(summary, key=summary.get)
    highest_amount = summary[highest_category]
    
    insights = {
        'highest_category': highest_category,
        'highest_amount': highest_amount,
        'suggestion': f"You spent the most on {highest_category} (${highest_amount:.2f}). Consider reviewing these expenses to find areas for saving."
    }
    return insights

def generate_pie_chart(summary, month_name):
    if not summary:
        return None

    labels = list(summary.keys())
    sizes = list(summary.values())

    fig = Figure(figsize=(6, 5), dpi=100)
    ax = fig.add_subplot(111)
    ax.pie(sizes, labels=labels, autopct='%1.1f%%', startangle=140)
    ax.set_title(f'Expense Breakdown for {month_name}')
    ax.axis('equal')
    
    return fig
