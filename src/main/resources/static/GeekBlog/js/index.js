let content = $('.terminal');
let terminalCanvas = $('#terminal-canvas');
let bgCanvas = $('#bg-canvas');

let height = content.css('height');
let width = content.css('width');

terminalCanvas.attr('height',height);
terminalCanvas.attr('width',width);
bgCanvas.attr('width',window.screen.width);
bgCanvas.attr('height',window.screen.height);

const canvas = jQuery('#terminal-canvas')[0];
const canvasBg = jQuery('#bg-canvas')[0];
const ctx = canvas.getContext('2d');
const ctxBg = canvasBg.getContext('2d');
//添加终端
const terminal = new Terminal(
    ctx,
    parseInt(height.substr(0,height.length-2)),
    parseInt(width.substr(0,width.length-2)),
    'Kcaogen@blog'
);
//添加背景数码流
const hacker = new Hacker(
    ctxBg,
    window.screen.width,
    window.screen.height
);

terminal.render();
terminal.input('help');
terminal.input('$${Enter}');

content.on("mousewheel DOMMouseScroll", function (e) {

    let delta = (e.originalEvent.wheelDelta && (e.originalEvent.wheelDelta > 0 ? 1 : -1)) ||  // chrome & ie
        (e.originalEvent.detail && (e.originalEvent.detail > 0 ? -1 : 1));              // firefox

    if (delta > 0) {
        terminal.scroll("up");
    } else if (delta < 0) {
        terminal.scroll("down");
    }
});

let over = false;
$('.head-pic-a').mouseover(function(){
    if(!over){
        over = true;
        terminal.input('$${Clear}');
        terminal.input('clear');
        terminal.input('$${Enter}');
        setTimeout(()=>{
            over = false;
            terminal.input('hello');
            terminal.input('$${Enter}');
        },300);
    }
});
