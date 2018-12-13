#import <UIKit/UIKit.h>
#import <React/RCTView.h>

@interface ZxingView : RCTView

@property (nonatomic, copy) NSString *text;
@property (nonatomic, copy) NSString *format;

@property (nonatomic, assign) int width;
@property (nonatomic, assign) int height;
@property (nonatomic, assign) bool cache;

@end
