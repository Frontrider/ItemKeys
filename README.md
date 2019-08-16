# ItemKeys
Adds a generic keybinding to interact with items

Implement `KeypressControlledItem`, or `ServerKeyPressControlledItem` on your item class to trigger an action when a key was pressed. The latter will send a message to the server and run the code on that side.
Implement `KeyholdingControlledItem` if you want it to run as long as the key was held down. Keyholding is currently not supported on the server.
