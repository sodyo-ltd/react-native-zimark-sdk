import React, { Component, Fragment } from 'react';
import {
  View,
  requireNativeComponent,
  NativeModules,
  NativeEventEmitter,
  Platform,
  StyleSheet,
} from 'react-native';

const { RNSodyoSdk } = NativeModules;

const eventEmitter = new NativeEventEmitter(RNSodyoSdk);

export default {
  init: (successCallback, errorCallback) => {
    return RNSodyoSdk.init(successCallback, errorCallback);
  },

  onError: (callback) => {
    eventEmitter.removeAllListeners('EventSodyoError');

    const subscription = eventEmitter.addListener('EventSodyoError', (e) => {
      if (typeof callback === 'function') {
        callback(e.error);
      }
    });

    return () => {
      return subscription.remove();
    };
  },

  onCloseScanner: (callback) => {
    if (Platform.OS === 'ios') {
      return () => undefined;
    }

    eventEmitter.removeAllListeners('EventCloseSodyoScanner');

    const subscription = eventEmitter.addListener('EventCloseSodyoScanner', () => {
      if (typeof callback === 'function') {
        callback();
      }
    });

    return () => {
      return subscription.remove();
    };
  },

  onResult: (callback) => {
    eventEmitter.removeAllListeners('EventContent');

    const subscription = eventEmitter.addListener('EventContent', (e) => {
      if (typeof callback === 'function') {
        const data = typeof e.data === 'string'
          ? JSON.parse(e.data)
          : e.data || [];
        callback(data);
      }
    });

    return () => {
      return subscription.remove();
    };
  },

  onFrameData: (callback) => {
    eventEmitter.removeAllListeners('OnFrameData');

    const subscription = eventEmitter.addListener('OnFrameData', (e) => {
      if (typeof callback === 'function') {
        const data = (e && e.data) || ''
        callback(data);
      }
    });

    return () => {
      return subscription.remove();
    };
  },

  onCloseContent: (callback) => {
    if (Platform.OS !== 'ios') {
      return () => undefined;
    }

    RNSodyoSdk.createCloseContentListener();
    eventEmitter.removeAllListeners('EventCloseSodyoContent');

    const subscription = eventEmitter.addListener('EventCloseSodyoContent', () => {
      if (typeof callback === 'function') {
        callback();
      }
    });

    return () => {
      return subscription.remove();
    };
  },

  start: (successCallback, errorCallback) => {
    eventEmitter.removeAllListeners('EventContent');

    RNSodyoSdk.start();

    eventEmitter.addListener('EventContent', (e) => {
      if (typeof successCallback === 'function') {
        successCallback(e.data);
      }
    });
  },

  removeAllListeners: () => {
    return eventEmitter.removeAllListeners();
  },

  close: () => {
    eventEmitter.removeAllListeners('EventContent');

    return RNSodyoSdk.close();
  },

  setUserInfo: (userInfo) => {
    return RNSodyoSdk.setUserInfo(userInfo);
  },

  saveNextFrameCapture: () => {
    return RNSodyoSdk.saveNextFrameCapture();
  },

  setScannerParams: (scannerPreferences) => {
    return RNSodyoSdk.setScannerParams(scannerPreferences);
  },

  addScannerParam: (key, value) => {
    return RNSodyoSdk.addScannerParam(key, value);
  },

  setDynamicProfileValue: (key, value) => {
    return RNSodyoSdk.setDynamicProfileValue(key, value);
  },

  setCustomAdLabel: (label) => {
    return RNSodyoSdk.setCustomAdLabel(label);
  },

  setAppUserId: (appUserId) => {
    return RNSodyoSdk.setAppUserId(appUserId);
  },

  setScanModes: (scanModes) => {
    return RNSodyoSdk.setScanModes(scanModes);
  },
};

export class Scanner extends Component {
  render () {
    const { children } = this.props;
    return (
      <Fragment>
        <RNSodyoSdkView style={{ height: '100%', width: '100%' }}/>

        <View style={styles.container} pointerEvents="box-none">
          {children}
        </View>
      </Fragment>
    );
  }
}

const RNSodyoSdkView = requireNativeComponent('RNSodyoSdkView', Scanner, {
  nativeOnly: {},
});

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    top: 0,
    bottom: 0,
    left: 0,
    right: 0,
    width: '100%',
    height: '100%',
    flex: 1,
  },
})
