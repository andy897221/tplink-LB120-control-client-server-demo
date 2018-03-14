var util = require('util');
var app = require('express')();
var http = require('http').Server(app);

const Bulb = require('tplink-lightbulb')
var andyLight = new Bulb("192.168.0.150")
var candyLight = new Bulb("192.168.0.178")
var MECdemo = new Bulb("192.168.3.110")

// toggle light related function
function changeStatus(status,res, light) {
    light.set(!status)
        .then(light_state => {
            res.send(""+light_state["on_off"])
        })
        .catch(err => {
            res.send(""+err)
        })
}

function offLight(res, light) {
    light.set(0)
        .then(light_state => {
            res.send(""+light_state["on_off"])
        })
        .catch(err => {
            res.send(""+err)
        })
}

function toggleLight(res, light) {
    light.info()
        .then(info => {changeStatus(info["light_state"]["on_off"],res, light)})
}

// change brightness related function
function brightnessChange(res, light, brightness_value) {
    light.set(true, 0, {brightness: parseInt(brightness_value)})
        .then(status => {
            res.send("Change Brightness --> "+parseInt(brightness_value))
        })
        .catch(err => {
            res.send(""+err)
        })
}

// trigger the actions when a GET request is made to the home directory page
app.get('/', function(req, res){
    // to know if the request is header based or url parameters based, and to store accordingly
    actionHeader = req.get('action')
    if(actionHeader == null) {
        actionHeader = req.query.action
        lightHeader = req.query.light
        clientHeader = req.query.client
    } else {
        lightHeader = req.get('light')
        clientHeader = req.get("client")
    }
    
    // to see which light bulb to request
    if(lightHeader == "andyLight")
        var light = andyLight
    else if(lightHeader == "candyLight")
        var light = candyLight
    else if(lightHeader == "MECdemo")
        var light = MECdemo
    
    // logging
    var logging = util.format('Requested. Client: %s. Light: %s. Action: %s',clientHeader,lightHeader,actionHeader)
    console.log(logging)
    
    // send to function corresponding to its action header
    if(actionHeader == "toggle") {
        toggleLight(res, light)
    } else if (actionHeader == "brightness") {
        var brightnessVal = req.get('brightness')
        if(brightnessVal == null) brightnessVal = req.query.brightness
        brightnessChange(res, light, brightnessVal)
    } else if (actionHeader == "off") {
        offLight(res, light)
    }
});

http.listen(3000, function(){
  console.log('listening on *:3000');
});
    
