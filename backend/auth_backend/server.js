// server.js

// set up ======================================================================
// get all the tools we need
var express        =  require('express')
var mongoose       =  require('mongoose')
var passport       =  require('passport')
var flash          =  require('connect-flash')
var morgan         =  require('morgan')
var cookieParser   =  require('cookie-parser')

var bodyParser     =  require('body-parser')
var session        =  require('express-session')
var methodOverride =  require('method-override')
var mongojs        =  require('mongojs')
var db             =  mongojs('Gamify')
var request        =  require('request')
var routes         =  require("./routes")

//var configDB       =  require('./config/database.js')

// server.js

// modules =================================================


// configuration ===========================================
//mongoose.connect(configDB.url);
mongoose.connect('mongodb://localhost/Gamify')

// config files
//var db = require('./config/db');

//set up app and connect to database =======================

var app = express()
// get all data/stuff of the body (POST) parameters
// parse application/json 
app.use(bodyParser())
app.use(morgan('dev'))
app.use(cookieParser())

//set up passport
app.use(session({ secret: 'wearethebestgamify'}))
app.use(passport.initialize())
app.use(passport.session())
app.use(flash())

app.set('trust proxy', function(ip){
	console.log(ip)
})

//set up routes
app.get('/', function (req, res)
			 { 
			 	res.send({serverUp:1})
			 }
		)

app.post('/api/signUp', routes.signUp)
app.post('/api/signIn', routes.signIn)
app.post('/api/localSignIn', routes.localSignIn)
//app.post('/api/facebookSignUp', routes.facebookSignUp)
//app.post('/api/facebookSignIn', routes.facebookSignIn)


// set-up port
app.listen(8080)
//Confirm app ran
console.log("Running at http://localhost:8080/")

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