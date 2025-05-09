const express = require('express');
const router = express.Router();
const User = require('./userModel');
const mongoose = require('mongoose'); // Import mongoose for ObjectId validation

// Registration (remains the same)
router.post('/register', async (req, res) => {
  const { username, password, role } = req.body;
  try {
    const user = new User({ username, password, role });
    await user.save();
    res.status(201).json({ message: 'User registered successfully.' });
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});

// Login (remains the same)
router.post('/login', async (req, res) => {
  const { username, password } = req.body;
  try {
    const user = await User.findOne({ username });
    if (!user || !(await user.comparePassword(password))) {
      return res.status(401).json({ error: 'Invalid credentials' });
    }
    res.status(200).json({ message: 'Login successful.' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// Get users by role (remains the same)
router.get('/', async (req, res) => {
    const { role } = req.query;
    try {
      let users;
      if (role) {
        users = await User.find({ role });
      } else {
        users = await User.find();
      }
      res.json(users);
    } catch (err) {
      res.status(500).json({ error: 'Server error' });
    }
  });

// Remove all users by role (rider or driver) (remains the same)
router.delete('/remove/role', async (req, res) => { // Changed the path to /remove/role to differentiate
  const { role } = req.body;
  if (!role || (role !== 'rider' && role !== 'driver')) {
    return res.status(400).json({ error: 'Please provide a valid role ("rider" or "driver") to remove.' });
  }
  try {
    const result = await User.deleteMany({ role: role });
    if (result.deletedCount > 0) {
      res.status(200).json({ message: `${result.deletedCount} ${role}s removed successfully.` });
    } else {
      res.status(404).json({ message: `No users found with the role "${role}".` });
    }
  } catch (error) {
    res.status(500).json({ error: 'Server error while removing users.' });
  }
});

// Remove a specific user by ID
router.delete('/remove/:userId', async (req, res) => {
  const { userId } = req.params;

  // Validate if userId is a valid ObjectId
  if (!mongoose.Types.ObjectId.isValid(userId)) {
    return res.status(400).json({ error: 'Invalid user ID format.' });
  }

  try {
    const user = await User.findByIdAndDelete(userId);
    if (!user) {
      return res.status(404).json({ message: `User with ID "${userId}" not found.` });
    }
    res.status(200).json({ message: `User with ID "${userId}" removed successfully.` });
  } catch (error) {
    res.status(500).json({ error: 'Server error while removing user.' });
  }
});

module.exports = router;