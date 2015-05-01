<%@ page import="java.util.*" %>
<%@ page import="com.jobber.core.Core" %>
<%
String url = request.getParameter("url");
String jobshtml = Core.getJobSuggestionsByLinkedUrl(url);
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
                    <li class="active">
                        <a href="recommendations.jsp"><i class="fa fa-fw fa-table"></i> Recommendations</a>
                    </li>
                    <li>
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
                                <i class="fa fa-file"></i> Recommendations
                            </li>
                        </ol>
                    </div>
                </div>
                <!-- /.row -->

                <div class="row">
                    <div class="col-lg-12">
                        <h2>Suggestions based on profile</h2>
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>Title</th>
                                        <th>Company</th>
                                        <th>Location</th>
                                        <th>Score</th>
                                        <th>Url</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <!-- Job recommendations -->
                                    <%= jobshtml %>
                                </tbody>
                            </table>

                            <hr>
                            <h5>CMPE 239 project by Gunja, Jihirsha, Omkar and Aishwarya</h5>
                            <br>
                            <br>

                        </div>
                    </div>
                </div>

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

</body>

</html>
