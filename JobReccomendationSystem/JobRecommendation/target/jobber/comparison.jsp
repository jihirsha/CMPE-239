<%@ page import="java.util.*" %>
<%@ page import="com.jobber.core.Core" %>
<%
String url = request.getParameter("url");
String morrisData = Core.getMorrisChartByLinkedUrl(url);
String morrisJobLegend = Core.getMorrisJobLegend();
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Job Recommendation System</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/sb-admin.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
</head>

<body>

    <div id="wrapper">

        <!-- Navigation -->
        <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.jsp" style="color:#fff">Job Recommendation System</a>
            </div>

            <!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
            <div class="collapse navbar-collapse navbar-ex1-collapse">
                <ul class="nav navbar-nav side-nav">
                    <li>
                        <a href="index.jsp"><i class="fa fa-fw fa-dashboard"></i> Dashboard</a>
                    </li>
                    <li>
                        <a href="recommendations.jsp"><i class="fa fa-fw fa-table"></i> Recommendations</a>
                    </li>
                    <li class="active">
                        <a href="comparison.jsp"><i class="fa fa-fw fa-bar-chart-o"></i> Comparison Charts</a>
                    </li>
                    <li>
                        <a href="aboutus.jsp"><i class="fa fa-fw fa-wrench"></i> About us</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </nav>

        <div id="page-wrapper">

            <div class="container-fluid">

                <!-- Page Heading -->
                <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">
                            Home
                            <small>Job recommendation based on LinkedIn profile</small>
                        </h1>
                        <ol class="breadcrumb">
                            <li>
                                <i class="fa fa-dashboard"></i>  <a href="index.jsp">Dashboard</a>
                            </li>
                            <li class="active">
                                <i class="fa fa-file"></i> Comparisons
                            </li>
                        </ol>
                    </div>
                </div>
                <!-- /.row -->

                <div class="row">
                    <div class="col-lg-12">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="panel panel-green">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-bar-chart-o"></i> Morris Chart Comparison of various similarity measures</h3>
                                        </div>
                                        <div class="panel-body">
                                            <div id="morris-area-chart"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- /.row -->
                        </div>
                    </div>
                </div>


                <div class="row">
                    <div class="col-lg-12">
                        <div class="table-responsive">
                            <table class="table table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>Id</th>
                                        <th>Job Title</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%= morrisJobLegend %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <hr>

                <!-- Bar char -->
                <div class="col-lg-12">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-bar-chart-o"></i> Bar Chart Comparison of various Cosine measures</h3>
                        </div>
                        <div class="panel-body">
                            <div id="morris-bar-chart-cosine"></div>
                        </div>
                    </div>
                </div>

                <!-- Bar char -->
                <div class="col-lg-12">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-bar-chart-o"></i> Bar Chart Comparison of various Jaccard measures</h3>
                        </div>
                        <div class="panel-body">
                            <div id="morris-bar-chart-jaccard"></div>
                        </div>
                    </div>
                </div>

                <!-- Bar char -->
                <div class="col-lg-12">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-bar-chart-o"></i> Bar Chart Comparison of various Pearson measures</h3>
                        </div>
                        <div class="panel-body">
                            <div id="morris-bar-chart-pearson"></div>
                        </div>
                    </div>
                </div>


                <!-- Bar char -->
                <div class="col-lg-12">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-bar-chart-o"></i> Bar Chart Comparison of various Euclidean measures</h3>
                        </div>
                        <div class="panel-body">
                            <div id="morris-bar-chart-euclidean"></div>
                        </div>
                    </div>
                </div>



                <hr>
                <h5>CMPE 239 project by Gunja, Jihirsha, Omkar and Aishwarya</h5>
                <br>
                <br>

            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

    <!-- Morris Charts JavaScript -->
    <script src="js/plugins/morris/raphael.min.js"></script>
    <script src="js/plugins/morris/morris.min.js"></script>
    <script>
        <%= morrisData %>
        <%= Core.getBarChartByLinkedUrl(url, "cosine") %>
        <%= Core.getBarChartByLinkedUrl(url, "jaccard") %>
        <%= Core.getBarChartByLinkedUrl(url, "pearson") %>
        <%= Core.getBarChartByLinkedUrl(url, "euclidean") %>
    </script>

</body>

</html>
