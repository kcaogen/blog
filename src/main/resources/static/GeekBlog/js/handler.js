/**
 * Created by eason on 16-11-4.
 */
var data = {
    "friendship-link":[
        {
            "name":"Estanball",
            "link":"http://wangbicong.cn",
            "summary":"聪哥哥的伊斯坦堡"
        },
        {
            "name":"Chestnutheng",
            "link":"http://chestnutheng.cn",
            "summary":"专注NLP\ML的技术博客"
        },
        {
            "name":"Shawn",
            "link":"http://shawnzeng.com",
            "summary":"一个爱捣鼓前端的产品汪"
        }
    ],
    "posts":[
        {
            "title":"String和short[]互相转换",
            "tag":[
                "Java"
            ],
            "date":"2017-08-04",
            "path":"/blogInfo/16"
        },
        {
            "title":"Thread.currentThread()和this的差异",
            "tag":[
                "Java",
                "多线程"
            ],
            "date":"2017-06-27",
            "path":"/blogInfo/15"
        },
        {
            "title":"Java 中的枚举 (enum)",
            "tag":[
                "Java"
            ],
            "date":"2017-06-09",
            "path":"/blogInfo/14"
        },
        {
            "title":"Docker+Solr+IK",
            "tag":[
                "Docker",
                "Solr",
                "Linux"
            ],
            "date":"2017-05-27",
            "path":"/blogInfo/13"
        },
        {
            "title":"Ubuntu14.04安装tomcat7.0和jdk1.7",
            "tag":[
                "Java",
                "Linux",
                "Tomcat"
            ],
            "date":"2017-05-23",
            "path":"/blogInfo/12"
        },
        {
            "title":"快速理解Java中的七种单例模式",
            "tag":[
                "Java",
                "设计模式"
            ],
            "date":"2017-05-19",
            "path":"/blogInfo/11"
        },
        {
            "title":"Cookie与Session的区别",
            "tag":[
                "Web"
            ],
            "date":"2017-05-17",
            "path":"/blogInfo/10"
        },
        {
            "title":"互联网协议入门（一）",
            "tag":[
                "Web"
            ],
            "date":"2017-05-17",
            "path":"/blogInfo/9"
        },
        {
            "title":"非常经典的JAVA编程题（兔子规律）",
            "tag":[
                "Java",
                "算法"
            ],
            "date":"2017-05-16",
            "path":"/blogInfo/8"
        },
        {
            "title":"非常经典的JAVA编程题（素数）",
            "tag":[
                "Java",
                "算法"
            ],
            "date":"2017-05-16",
            "path":"/blogInfo/7"
        },
        {
            "title":"非常经典的JAVA编程题（水仙花数）",
            "tag":[
                "Java",
                "算法"
            ],
            "date":"2017-05-16",
            "path":"/blogInfo/6"
        },
        {
            "title":"非常经典的JAVA编程题（正整数分解质因数）",
            "tag":[
                "Java",
                "算法"
            ],
            "date":"2017-05-16",
            "path":"/blogInfo/5"
        },
        {
            "title":"非常经典的JAVA编程题（条件运算符）",
            "tag":[
                "Java",
                "算法"
            ],
            "date":"2017-05-16",
            "path":"/blogInfo/4"
        },
        {
            "title":"CentOS6.5安装Redis3.0稳定版",
            "tag":[
                "Linux",
                "Redis"
            ],
            "date":"2017-05-16",
            "path":"/blogInfo/3"
        },
        {
            "title":"Nginx学习之合并请求连接加速网站访问",
            "tag":[
                "Linux",
                "Nginx"
            ],
            "date":"2017-05-15",
            "path":"/blogInfo/2"
        },
        {
            "title":"Tomcat内存增长分析",
            "tag":[
                "Java",
                "Tomcat",
                "JVM"
            ],
            "date":"2017-05-15",
            "path":"/blogInfo/1"
        }
    ]
}

let __tag = [];
let hello = [`
    There is no place like
     1  2  7  .  0  .  0  .  1
`,
    `
    这是因特网不为人知的角落
    也许整整一年只有你的访问
    那么
    请无论如何
    阅读愉快             --kcaogen
`
];

let cmdHandler = {
    'help':function([cmd,reg]){
        let value = '',i=1;
        for(let x of Object.keys(cmdHandler)){
            value += `  ${i++}.  ${x}\n`;
        }
        return new Promise((resolve)=>{
            resolve(value);
        })
    },
    'article':function([cmd,id]){
        let value = '';
        if(id){
            if(id>data.posts.length-1) value = `No match with id:${id}!`;
            else {
                value = 'Waiting...\nFinding...';
                setTimeout(()=>window.location.href=data.posts[id].path,500);
            }
        }else{
            for(let index in data.posts){
                value += `id '${index}'    :    title '${data.posts[index].title}'`+'\n';
            }
            value+=`type 'article <id>' to read the article by id!`;
        }
        return new Promise((resolve)=>{
            resolve(value);
        })
    },
    'tag':function([cmd,id]){
        let value = '',i=1;
        if(id){
            if(id>__tag.length) value = `No match with id ${id}!`;
            else{
                value += `TAG:${__tag[id-1]}`+'\n';
                for(let index in data.posts){
                    for(let tag of data.posts[index].tag){
                        if(tag==__tag[id-1]){
                            value += `id '${index}'    :    title '${data.posts[index].title}'`+'\n';
                        }
                    }
                }
                value+=`type 'article <id>' to read the article by id!`;
            }
        }else{
            __tag.length=0;
            for(let index in data.posts){
                for(let tag of data.posts[index].tag){
                    if(!value.includes(tag)){
                        __tag.push(tag);
                        value += `id '${i++}'     :    TAG '${tag}'`+'\n';
                    }
                }
            }
            value+=`type 'tag <id>' to look through all article of the tag!`;
        }
        return new Promise((resolve)=>{
            resolve(value);
        })
    },
    'friendship':function([cmd,id]){
        let value = '';
        if(id){
            if(id>data['friendship-link'].length-1) value = `No match with id:${id}!`;
            else {
                value = 'Waiting...\nFinding...';
                setTimeout(()=>window.location.href=data['friendship-link'][id].link,500);
            }
        }else{
            for(let index in data['friendship-link']){
                value += `id '${index}'    :    ${data['friendship-link'][index].name}|${data['friendship-link'][index].summary}`+'\n';
            }
            value+=`type 'friendship <id>' to visit their blogs!`;
        }
        return new Promise((resolve)=>{
            resolve(value);
        })
    },
    'hello':function([cmd]){
        let value = '';
            value=hello[Math.round(Math.random()*(hello.length-1))];
        return new Promise((resolve)=>{
            resolve(value);
        })
    },
    'clear':function([cmd]){
        return new Promise((resolve)=>{
            resolve("${clear}");
        })
    }
};
