// server.js

// modules =================================================
var express        = require('express')
var bodyParser     = require('body-parser')
var methodOverride = require('method-override')
var mongojs        = require('mongojs')
var db             = mongojs('test')
var request        = require('request')
var routes         = require("./routes")

// configuration ===========================================
    
// config files
//var db = require('./config/db');

//set up app and connect to database =======================
app = express()

// get all data/stuff of the body (POST) parameters
// parse application/json 
app.use(bodyParser())

//set up routes
app.get('/', function (req, res)
			 { 
			 	res.send({serverUp:1})
			 }
		)

//app.post('/api/getuserid', routes.getUserId)
app.post('/api/storeXYZ', routes.storeXYZ)
app.post('/api/storeAct', routes.storeAct)
app.post('/api/getData', routes.getData)
app.post('/api/storeTestConfirm', routes.storeTestConfirm)
app.post('/api/storeScore', routes.storeScore)
app.post('/api/storeFood', routes.storeFood)
app.post('/api/storeActTime', routes.storeActTime)
app.post('/api/storeRooms', routes.storeRooms)
app.post('/api/getScore', routes.getScore)
app.post('/api/getFood', routes.getFood)
app.post('/api/getActTime', routes.getActTime)
app.post('/api/getRooms', routes.getRooms)




// set-up port
app.listen(3000)
//Confirm app ran
console.log("Running at http://localhost:3000/")

// parse application/vnd.api+json as json
//app.use(bodyParser.json({ type: 'application/vnd.api+json' })); 

// parse application/x-www-form-urlencoded
//app.use(bodyParser.urlencoded({ extended: true })); 

// override with the X-HTTP-Method-Override header in the request. simulate DELETE/PUT
//app.use(methodOverride('X-HTTP-Method-Override')); 

// set the static files location /public/img will be /img for users
//app.use(express.static(__dirname + '/public')); 

// expose app           
//exports = module.exports = app; 
