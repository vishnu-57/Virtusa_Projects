import tkinter as tk
from tkinter import ttk, messagebox
from datetime import datetime
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from storage import add_expense, load_expenses
from analytics import get_monthly_summary, get_insights, generate_pie_chart

class ExpenseTrackerGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Smart Expense Tracker")
        self.root.geometry("800x600")

        # Create Layout
        self.left_frame = tk.Frame(root, width=250, bg="#f0f0f0")
        self.left_frame.pack(side=tk.LEFT, fill=tk.Y, padx=10, pady=10)

        self.right_frame = tk.Frame(root, bg="white")
        self.right_frame.pack(side=tk.RIGHT, fill=tk.BOTH, expand=True, padx=10, pady=10)

        self.setup_input_form()
        self.setup_summary_view()

    def setup_input_form(self):
        tk.Label(self.left_frame, text="Add Expense", font=("Arial", 14, "bold"), bg="#f0f0f0").pack(pady=10)

        tk.Label(self.left_frame, text="Date (YYYY-MM-DD):", bg="#f0f0f0").pack(anchor="w")
        self.date_entry = tk.Entry(self.left_frame)
        self.date_entry.insert(0, datetime.today().strftime('%Y-%m-%d'))
        self.date_entry.pack(fill=tk.X, pady=5)

        tk.Label(self.left_frame, text="Category:", bg="#f0f0f0").pack(anchor="w")
        self.category_cb = ttk.Combobox(self.left_frame, values=["Food", "Travel", "Bills", "Shopping", "Entertainment", "Other"])
        self.category_cb.pack(fill=tk.X, pady=5)

        tk.Label(self.left_frame, text="Amount:", bg="#f0f0f0").pack(anchor="w")
        self.amount_entry = tk.Entry(self.left_frame)
        self.amount_entry.pack(fill=tk.X, pady=5)

        tk.Label(self.left_frame, text="Description:", bg="#f0f0f0").pack(anchor="w")
        self.desc_entry = tk.Entry(self.left_frame)
        self.desc_entry.pack(fill=tk.X, pady=5)

        tk.Button(self.left_frame, text="Add Expense", bg="#4CAF50", fg="white", font=("Arial", 10, "bold"), command=self.handle_add_expense).pack(fill=tk.X, pady=15)

        tk.Label(self.left_frame, text="View Features", font=("Arial", 14, "bold"), bg="#f0f0f0").pack(pady=10)
        
        tk.Label(self.left_frame, text="Year/Month (YYYY-MM):", bg="#f0f0f0").pack(anchor="w")
        self.date_filter = tk.Entry(self.left_frame)
        self.date_filter.insert(0, datetime.today().strftime('%Y-%m'))
        self.date_filter.pack(fill=tk.X, pady=5)

        tk.Button(self.left_frame, text="Show Summary & Chart", command=self.handle_show_chart).pack(fill=tk.X, pady=5)
        tk.Button(self.left_frame, text="Show Insights", command=self.handle_show_insights).pack(fill=tk.X, pady=5)

    def setup_summary_view(self):
        self.summary_text = tk.Text(self.right_frame, height=8, font=("Arial", 12))
        self.summary_text.pack(fill=tk.X, pady=5)
        self.summary_text.insert(tk.END, "Welcome to Smart Expense Tracker.\nAdd expenses or use the panel to view reports.")
        self.summary_text.config(state=tk.DISABLED)

        self.chart_frame = tk.Frame(self.right_frame, bg="white")
        self.chart_frame.pack(fill=tk.BOTH, expand=True)

    def handle_add_expense(self):
        date = self.date_entry.get().strip()
        cat = self.category_cb.get().strip()
        amt_str = self.amount_entry.get().strip()
        desc = self.desc_entry.get().strip()

        if not date or not cat or not amt_str:
            messagebox.showerror("Error", "Date, Category, and Amount are required.")
            return

        try:
            amt = float(amt_str)
            if amt <= 0: raise ValueError
        except ValueError:
            messagebox.showerror("Error", "Amount must be a positive number.")
            return

        add_expense(date, cat, amt, desc)
        messagebox.showinfo("Success", "Expense added successfully!")
        self.amount_entry.delete(0, tk.END)
        self.desc_entry.delete(0, tk.END)

    def _get_selected_ym(self):
        ym = self.date_filter.get().strip()
        try:
            dt = datetime.strptime(ym, '%Y-%m')
            return dt.year, dt.month, dt.strftime('%B %Y')
        except ValueError:
            messagebox.showerror("Error", "Invalid Date format for filtering. Use YYYY-MM")
            return None, None, None

    def handle_show_chart(self):
        year, month, name = self._get_selected_ym()
        if not year: return

        summary, total = get_monthly_summary(year, month)
        self.summary_text.config(state=tk.NORMAL)
        self.summary_text.delete('1.0', tk.END)

        if not summary:
            self.summary_text.insert(tk.END, f"No expenses found for {name}.")
            self.summary_text.config(state=tk.DISABLED)
            for widget in self.chart_frame.winfo_children():
                widget.destroy()
            return

        text = f"Summary for {name} (Total: ${total:.2f}):\n"
        for k, v in summary.items():
            text += f" - {k}: ${v:.2f}\n"
        
        self.summary_text.insert(tk.END, text)
        self.summary_text.config(state=tk.DISABLED)

        # Clear old chart
        for widget in self.chart_frame.winfo_children():
            widget.destroy()

        fig = generate_pie_chart(summary, name)
        if fig:
            canvas = FigureCanvasTkAgg(fig, master=self.chart_frame)
            canvas.draw()
            canvas.get_tk_widget().pack(fill=tk.BOTH, expand=True)

    def handle_show_insights(self):
        year, month, name = self._get_selected_ym()
        if not year: return

        summary, total = get_monthly_summary(year, month)
        self.summary_text.config(state=tk.NORMAL)
        self.summary_text.delete('1.0', tk.END)

        if not summary:
            self.summary_text.insert(tk.END, f"No expenses found for {name}.")
        else:
            insights = get_insights(summary)
            if insights:
                self.summary_text.insert(tk.END, f"Insights for {name}:\n\n" + insights['suggestion'])
        
        self.summary_text.config(state=tk.DISABLED)

if __name__ == "__main__":
    root = tk.Tk()
    app = ExpenseTrackerGUI(root)
    root.mainloop()
