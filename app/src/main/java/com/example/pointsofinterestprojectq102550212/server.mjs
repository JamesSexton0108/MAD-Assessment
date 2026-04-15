// Import dependencies
import express from 'express';
import Database from 'better-sqlite3';

// Create our Express server.
const app = express();

// Enable reading JSON from the request body of POST requests
app.use(express.json());
app.use(express.urlencoded({extended: false}));

// Load the database.
const db = new Database("pointsofinterest.db");

// Find all accommodation
app.get('/poi/all', (req, res) => {
    try {
        const stmt = db.prepare('SELECT * FROM pointsofinterest ORDER BY id');
        const results = stmt.all();
        res.json(results);
    } catch(error) {
        res.status(500).json({error: error});
    }
});

// Add a song
app.post('/poi/create', (req, res) => {
    try {
        const stmt = db.prepare('INSERT INTO pointsofinterest(name, type, description, lat, lon) VALUES(?,?,?,?,?)');
        const info = stmt.run(req.body.name, req.body.type, req.body.description, req.body.lat, req.body.lon);
        res.send(`${info.lastInsertRowid}`);
    } catch(error) {
        res.status(500).json({error: error});
    }
});

const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}.`);
});
