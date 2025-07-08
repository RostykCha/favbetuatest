FROM gradle:8.2.1-jdk17-alpine

USER root
RUN apk add --no-cache \
      chromium \
      chromium-chromedriver \
      nss \
      freetype \
      ttf-dejavu \
      dumb-init

RUN ln -sf /usr/bin/chromium-browser /usr/local/bin/google-chrome \
    && ln -sf /usr/lib/chromium/chromedriver /usr/local/bin/chromedriver

ENV CHROME_BIN=/usr/bin/chromium-browser
ENV browser=chrome
ENV _JAVA_OPTIONS="-Djava.awt.headless=true"

WORKDIR /automation-framework-example
COPY . .


RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle clean testClasses --no-daemon

ENTRYPOINT ["/usr/bin/dumb-init", "--"]   # cleans PID 1 signals for Gradle
CMD ["gradle", "test", "-x", "clean", "--no-daemon"]
