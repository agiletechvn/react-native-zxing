#import "ZxingView.h"
#import "ZxingViewManager.h"

@implementation ZxingViewManager

RCT_EXPORT_MODULE()

- (RCTView *)view
{
    return [[ZxingView alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(text, NSString);
RCT_EXPORT_VIEW_PROPERTY(format, NSString);
RCT_EXPORT_VIEW_PROPERTY(width, int);
RCT_EXPORT_VIEW_PROPERTY(height, int);
RCT_EXPORT_VIEW_PROPERTY(cache, BOOL);

@end

