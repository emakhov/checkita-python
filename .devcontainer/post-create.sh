#!/bin/bash

echo "Running post-create script..."

# Update package lists
sudo apt-get update

# Setup Coursier for current user
echo "Setting up Coursier for current user..."
cs setup --yes 

echo 'export PATH="$HOME/.local/share/coursier/bin:$PATH"' >> ~/.bashrc

# Configure git
git config --global user.name "Egor Makhov"
git config --global user.email e.makhov@protonmail.com

# Set appropriate permissions for workspace
sudo chown -R $USER:$USER /workspace