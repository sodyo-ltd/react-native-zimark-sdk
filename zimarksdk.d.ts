import * as React from 'react'
import { EResultScanType } from './constants'

declare const _default: {
  init (successCallback?: () => void, errorCallback?: (msg: string) => void): void
  onError (callback: (err: string) => void): () => void,
  onCloseScanner (callback: () => void): () => void,
  onCloseContent (callback: () => void): () => void,
  start (successCallback?: (immediateData?: string) => void, errorCallback?: (msg: string) => void): void
  close (): void
  setUserInfo (userInfo: { [key: string]: string | number }): void
  setScannerParams (scannerPreferences: { [key: string]: string }): void
  addScannerParam (key: string, value: string): void
  setDynamicProfileValue (key: string, value: string): void
  setCustomAdLabel (label: string): void
  setAppUserId (appUserId: string): void
  setScanModes (modes: EResultScanType[]): void
  removeAllListeners (eventType?: string): void
  onResult (callback: (data: IResult[]) => void): () => void,
  onColorCalibrationResult (callback: (data: ICalibrationResult) => void): () => void,
  onFrameData (callback: (value: string) => void): () => void,
  saveNextFrameCapture: () => void,
  calibrateColors: (controlMarkerCode: string, printedMarkerCode: string) => void,
}

export interface IScannerProps {
}

export interface IResult {
  height: number
  result: string
  scanType: EResultScanType
  screenAdjustedCoordinates: {
    bottom: number
    left: number
    right: number
    top: number
  }
  width: number
  x: number
  y: number
}

export interface ICalibrationResult {
  colorTable: number[][]
  controlMarker: string
  printerMarker: string
}


export declare class Scanner extends React.PureComponent<IScannerProps> {
}

export default _default
