FROM ubuntu

#install all the tools you might want to use in your container
RUN apt-get update
RUN apt-get install curl -y
RUN apt-get install vim -y
#the following ARG turns off the questions normally asked for location and timezone for Apache
ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get install apache2 -y

#change working directory to root of apache webhost

WORKDIR var/www/
RUN mkdir test
WORKDIR test/
RUN mkdir html
WORKDIR html/
RUN touch index.html

COPY index-test.html index.html

WORKDIR /etc/apache2/sites-available

COPY appopsbr.conf appopsbr.conf

#CMD ["apache2ctl", "-D", "FOREGROUND"]

RUN a2enmod proxy
RUN a2enmod proxy_http
RUN a2enmod headers
RUN a2enmod rewrite
RUN a2enmod proxy_fcgi 
RUN a2enmod proxy_balancer 

#CMD ["apache2ctl", "-D", "FOREGROUND"]

RUN a2dissite 000-default.conf
RUN a2ensite appopsbr.conf

CMD ["apache2ctl", "-D", "FOREGROUND"]
