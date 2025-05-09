require('dotenv').config();
const express = require('express');
const { createClient } = require('redis');

const app = express();
const port = process.env.PORT || 5000;

const redisClient = createClient({ url: process.env.REDIS_URL });
redisClient.connect().then(() => {
  console.log('Connected to Redis');
});

app.get('/location/:driverId', async (req, res) => {
  const location = await redisClient.get(`driver:${req.params.driverId}`);
  res.send({ driverId: req.params.driverId, location });
});

app.listen(port, () => {
  console.log(`Location Service running on port ${port}`);
});
