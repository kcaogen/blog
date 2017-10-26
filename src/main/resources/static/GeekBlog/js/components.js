/**
 * Created by eason on 16-11-4.
 */
class Component {
    constructor(ctx,color,width,height){
        this.ctx = ctx;
        this.color = color;
        this.width = width;
        this.height = height;
    }
    draw(x,y){console.log('need override the method of drawing!')};
}

class Cursor extends Component{
    constructor(ctx,color,width,height){
        super(ctx,color,width,height)
        this.active = true;
        this.flag = true;
        this.time = 0;
        this.speed = 36;
    }

    draw(x,y){
        if(this.active){
            this.time++;
            if(this.time>=this.speed){
                this.time=0;
                this.flag = !this.flag;
            }

            if(this.flag){
                ctx.clearRect(x,y,this.width,this.height);
            } else{
                ctx.fillStyle = this.color;
                ctx.fillRect(x,y,this.width,this.height);
            }
        }
    };
}

class Prompt extends Component{
    constructor(ctx,color,width,height,user){
        super(ctx,color,width,height)
        this.user = user;
        this.fontWidth = ctx.measureText(this.user).width;
        if(this.fontWidth > this.width*7/8-20) this.width = (this.fontWidth+20)*8/7;
    }

    draw(x,y){
        ctx.fillStyle = this.color;
        ctx.beginPath();
        ctx.moveTo(x,y);
        ctx.lineTo(x+this.width*7/8,y);
        ctx.lineTo(x+this.width,y+this.height/2);
        ctx.lineTo(x+this.width*7/8,y+this.height);
        ctx.lineTo(x,y+this.height);
        ctx.lineTo(x,y);
        ctx.fill();

        ctx.fillStyle = 'white';
        ctx.fillText(this.user, x+(this.width*7/8-this.fontWidth)/2, y+this.height*3/5);
    };
}