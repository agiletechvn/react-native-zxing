// @flow

import React, { Component } from 'react';
import { requireNativeComponent } from 'react-native';

type Format =
  | 'AZTEC'
  | 'CODABAR'
  | 'CODE_39'
  | 'CODE_93'
  | 'CODE_128'
  | 'DATA_MATRIX'
  | 'EAN_8'
  | 'EAN_13'
  | 'ITF'
  | 'MAXICODE'
  | 'PDF_417'
  | 'QR_CODE'
  | 'RSS_14'
  | 'RSS_EXPANDED'
  | 'UPC_A'
  | 'UPC_E'
  | 'UPC_EAN_EXTENSION';

type Props = {
  width: number,
  height: number,
  foo: number,
  text: string,
  format: Format
};

const RNZxing = requireNativeComponent('ZxingView', null);
export default class ZxingView extends Component<Props> {
  render() {
    return <RNZxing {...this.props} />;
  }
}
