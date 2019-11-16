const path = require('path');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const OptimizeCSSAssetsPlugin = require('optimize-css-assets-webpack-plugin');

module.exports = {
  mode: 'production',
  entry: './resources/frontend/index.js',
  output: {
    path: path.resolve(__dirname, 'resources', 'assets'),
    filename: 'bundle.js',
    publicPath: '/'
  },
  optimization: {
    minimizer: [
      new UglifyJsPlugin({
        cache: true,
        parallel: true,
        sourceMap: true
      }),
      new OptimizeCSSAssetsPlugin({})
    ]
  },
  plugins: [
    new MiniCssExtractPlugin({
      filename: "bundle.css",
      chunkFilename: "[id].css"
    })
  ],
  module: {
    rules: [
      {
        test: /\.(scss)$/,
        use: [
          {
            loader: MiniCssExtractPlugin.loader,
          },
          {
            loader: 'css-loader'
          },
          {
            loader: 'postcss-loader',
            options: {
              plugins: function () {
                return [
                  require('precss'),
                  require('autoprefixer')
                ];
              }
            }
          },
          {
            loader: 'sass-loader'
          }
        ]
      },
      {
        test: /\.(png|gif|jpe|jpg|woff|woff2|eot|ttf|svg)(\?.*$|$)?/,
        use: [{
          loader: 'url-loader',
          options: {
            name: '[name].[ext]?[hash]',
            outputPath: 'fonts/'
          }
        }]
      }
    ]
  }
};
