var mongoose = require('mongoose');

//define models
//module.exports allows passing of this to other files when it is called
module.exports = mongoose.model('user', 
								 {
								 	name : {type : String, default: ''
								 	id : {type : String, default: ''}
								 }
								 );
