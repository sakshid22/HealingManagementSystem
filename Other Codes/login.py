import Tkinter as tk
import tkFileDialog as tkfd
import tkMessageBox as tkmb
#import xlrd


#GUI setup
root = tk.Tk()
root.title("TLA Database Tool")

user_label = tk.Label(root, text = "username").pack()
user = tk.Entry(root, textvariable = tk.StringVar()).pack()
pw_label = tk.Label(root, text = "password").pack()
#pw = tk.Entry(root, show = "*", textvariable = tk.StringVar()).pack()
#login_button = tk.Button(root, text = "Login", command = lambda: logintoDB(user,pw)).pack()
root.mainloop()