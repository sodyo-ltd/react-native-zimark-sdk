
# React Native Zimark SDK Plugin that wraps Zimark sdk for Android and iOS

## Install
    npm i @sodyo/react-native-zimark-sdk -E

If you using version of react-native < 60 then you have to make a link

    react-native link
    
[Requires multidex support for android](https://medium.com/@aungmt/multidex-on-androidx-for-rn-0-60-x-cbb37c50d85)

## Quick start
```
import ZimarkSdk from '@sodyo/react-native-zimark-sdk'

SodyoSDK.init(
    function(){ /* successful init callback */ },
    function(){ /* fail init callback */}
)
```

Use scanner as fragment (only after initialize ZimarkSDK)
```
import { Scanner } from '@sodyo/react-native-zimark-sdk'
...
<Scanner>
    <Text>Children on top of the scanner</Text>
</Scanner>
```
`isEnabled toggles the Scanners active / pause status.`

Set the Zimark error listener
```
ZimarkSdk.onError(
    function(err){ /* fail callback */ }
)
```
`For unsubscribing just call the returned function`

Personal User Information (some object)

```
ZimarkSdk.setUserInfo(userInfo)
```

Setting Scanner Preferences (some flat object)
```
ZimarkSdk.setScannerParams(scannerPreferences)
```

Personalized Content
```
ZimarkSdk.setCustomAdLabel(label)
```
`The label may include one or more tags in comma-separated values (CSV) format as follows: “label1,label2,label3”`

Remove all listeners
```
ZimarkSdk.removeAllListeners()
```

If you get an error similar to this when building Android:
```
More than one file was found with OS independent path 'META-INF/DEPENDENCIES'
```

then add in your `android/app/build.gradle` file add the following:
```
android {
    ...

    packagingOptions {
        ...

        exclude "META-INF/DEPENDENCIES.txt"
        exclude "META-INF/LICENSE.txt"
        exclude "META-INF/NOTICE.txt"
        exclude "META-INF/NOTICE"
        exclude "META-INF/LICENSE"
        exclude "META-INF/DEPENDENCIES"
        exclude "META-INF/notice.txt"
        exclude "META-INF/license.txt"
        exclude "META-INF/dependencies.txt"
        exclude "META-INF/LGPL2.1"
        exclude "META-INF/ASL2.0"
        exclude "META-INF/maven/com.google.guava/guava/pom.properties"
        exclude "META-INF/maven/com.google.guava/guava/pom.xml"
    }
}
```
