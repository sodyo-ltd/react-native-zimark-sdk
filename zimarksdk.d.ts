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
  onFrameData (value: string): () => void,
  saveNextFrameCapture: () => void,
}

export interface IScannerProps {
  isEnabled?: boolean
}

export interface IResult {
  height: number
  result: string
  scanType: EResultScanType
  width: number
  x: number
  y: number
}

export declare class Scanner extends React.PureComponent<IScannerProps> {
}

export default _default
