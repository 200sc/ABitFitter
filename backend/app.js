// Import stuff
var express    = require('express')
var bodyParser = require('body-parser')
var mongojs    = require('mongojs')
var routes     = require('./routes')

// Set up the App and connect to DB
app = express()
app.use(bodyParser())

// Set up the routes
app.get('/', function (req, res){ res.send({serverUp:1}) })
app.post('/api/getuserid', routes.getUserId)
app.post('/api/getuserrooms', routes.getUserRooms)
app.post('/api/getuseritems', routes.getUserItems)
app.post('/api/getuserinfo', routes.getUserInfo)


// Run App
app.listen(3000)
console.log("Running at http://localhost:3000/")