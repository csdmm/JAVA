<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>购物车列表</title>
    <style type="text/css">
        body,table{
            font-size:12px;
        }
        table{
            table-layout:fixed;
            empty-cells:show;
            border-collapse: collapse;
            margin:0 auto;
        }
        td{
            height:30px;
        }
        h1,h2,h3{
            font-size:12px;
            margin:0;
            padding:0;
        }
        .table{
            border:1px solid #cad9ea;
            color:#666;
        }
        .table th {
            background-repeat:repeat-x;
            height:30px;
        }
        .table td,.table th{
            border:1px solid #cad9ea;
            padding:0 1em 0;
        }
        .table tr.alter{
            background-color:#f5fafe;
        }
    </style>
</head>
<body>
<div style="padding-left: 5%; font-size: x-large;">购物车列表</div>
<table width="90%" class="table">

    <tr>
        <th>商品编号</th>
        <th>商品名称</th>
        <th>商品图片</th>
        <th>商品单价（元）</th>
        <th>购买数量</th>
        <th>操作</th>
    </tr>
    <c:forEach items="${cartList}" var="cart">
        <tr>
            <td style="text-align: center">${cart.id}</td>
            <td style="text-align: center">${cart.goodsName}</td>
            <td style="text-align: center"><img src="${cart.picture}"/></td>
            <td style="text-align: center">${cart.price}</td>
            <td style="text-align: center">${cart.num}</td>
            <td style="text-align: center;color: #a94442" >
                <a onclick="delCart(${cart.id})" href="#">移除购物车</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
<script type="text/javascript" src="static/js/layui/layui.js"></script>
<script >
    layui.use('layer', function() {
        var $ = layui.jquery, layer = layui.layer;
    });
    //移除购物车
    function delCart(id) {
        var $ = layui.jquery, layer = layui.layer;
        layer.confirm('确定移除购物车吗？', {
            btn: ['确定','取消']
        }, function(){
            $.ajax({
                url:"shopCart?action=delCart",
                type:'post',
                data:{username:username,pwd:pwd},
                success:function(data){
                    if(data==0){
                        window.location.href="goodsList";
                    }else{
                        alert("登录失败，账号或者密码错误");
                    }
                }
            });
        }, function(){
            layer.msg('客官，新鲜的水果，过了这个村没有这家店了哟', {
                time: 2000,
            });
        });
    }
</script>
</html>