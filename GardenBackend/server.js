// receive data from serial line
const { SerialPort } = require('serialport')

const { ReadlineParser } = require('@serialport/parser-readline')
const port = new SerialPort({ path:'COM5' , baudRate: 9600})

const parser = port.pipe(new ReadlineParser({ delimiter: '\n' }))
 //read the port data
port.on('open', function () {
    console.log('open');
});
parser.on('data', function (data) {
    console.log('Yo shorty its your birthday:',data);
});
// send data to serial line
function sendData(){
    port.write('0\n', function(err) {
        if (err) {
            return console.log('Error on write: ', err.message);
        }
        console.log('message written');
    });
}

setInterval(sendData, 1000);