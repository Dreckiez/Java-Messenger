echo "Compiling source files..."
javac -cp "lib/*:." -d out app/*.java screens/*.java

echo "Copying assets..."
mkdir -p out/assets
cp -R assets/* out/assets/

echo "Running app..."
java -cp "out:lib/*" app.Main
