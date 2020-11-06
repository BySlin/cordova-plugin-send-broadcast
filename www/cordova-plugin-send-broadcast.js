var exec = require("cordova/exec");

exports.sendAction = function (action, success, error) {
  exec(success, error, "cordova-plugin-send-broadcast", "start", [action]);
};
