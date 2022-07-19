//ESP32S3 PART

const express = require('express')
const bodyParser = require('body-parser')
var temp = 0;
var light = 8;
var irrigationStatus = 0;
// Create a new instance of express
const app = express()

// Tell express to use the body-parser middleware and to not parse extended bodies
app.use(bodyParser.urlencoded({ extended: false }));

// Parse JSON bodies (as sent by API clients)
app.use(express.json());

//create / route
app.get('/', (req, res) => {
  res.send("Irrigation: "+irrigationStatus+ " Temperature: " + temp + " Light: " + light);
});

// Access the parse results as request.body
app.post('/', function(request, response){
    //console.log("Temperature: "+request.body.temp);
    temp = request.body.temp;
    light = request.body.light;
    //console.log("Lights level: "+request.body.light);
});

// Tell our app to listen on port 12345
app.listen(12345, function (err) {
  if (err) {
    throw err
  }

  console.log('Server started on port 12345')
});


// ARDUINO PART

const { SerialPort } = require('serialport')

const { ReadlineParser } = require('@serialport/parser-readline')
const port = new SerialPort({ path:'COM5' , baudRate: 9600})

const parser = port.pipe(new ReadlineParser({ delimiter: '\n' }))
 //read the port data
port.on('open', function () {
    console.log('open');
});
parser.on('data', function (data) {
    console.log('Data:',data);
    if(data.includes('irrigation')){
        irrigationStatus = data.split(':')[1];
        console.log("Irrigation: "+irrigationStatus);
    }
});
// send data to serial line
function sendData(){
    port.write(temp+','+light+':', function(err) {
        if (err) {
            return console.log('Error on write: ', err.message);
        }
    });
}

setInterval(sendData, 1000);