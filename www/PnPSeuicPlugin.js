var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'PnPSeuicPlugin', 'coolMethod', [arg0]);
};


exports.callBackToJavascript = function (arg0, success, error) {
    exec(success, error, 'PnPSeuicPlugin', 'coolMethod', [ arg0 ]);
};
