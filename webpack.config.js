const path = require('path');
const webpack = require('webpack');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const WebpackChunkHash = require("webpack-chunk-hash");
const CleanWebpackPlugin = require('clean-webpack-plugin');
const OptimizeCssAssetsPlugin = require('optimize-css-assets-webpack-plugin');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
// const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const CopyPlugin = require('copy-webpack-plugin');
const BabelMinifyPlugin = require('babel-minify-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const HtmlHarddiskPlugin = require('html-webpack-harddisk-plugin');
const PreloadWebpackPlugin = require('preload-webpack-plugin');
const cssnano = require('cssnano');

const styleExtractor = new ExtractTextPlugin({
    filename: "[name].[contenthash].css",
    allChunks: true,
    disable: process.env.NODE_ENV != "production"
});

const config = {
    entry: {
      main: "./public/javascripts/app.js"
    },
    devtool: "eval-source-map",
    output: {
        path: path.resolve(__dirname, 'public/dist'),
        filename: '[chunkhash].js',
        chunkFilename: "[chunkhash].js",
        publicPath: "/public/dist/"
    },
    devServer: {
      proxy: {
        "/": "http://localhost:9000"
      }
    },
    module: {
      // disable other momentjs locales loading
      noParse: [/moment.js/],
      rules: [
        {
          test: /\.modernizrrc$/,
          use: {
            loader: 'json-loader'
          }
        },
        {
          test: /\.js$/,
          exclude: /node_modules/,
          use: [
            {
              loader: 'babel-loader',
              options: { presets: ['es2015'] },
            },
            {
              loader: 'eslint-loader'
            }
          ],
        },
        {
          test: /\.(eot|woff|woff2|ttf)$/,
          use: { loader: 'url-loader', options: { limit: 40000 }},
        },
        {
          test: /\.(jpe?g|png|gif|svg)$/,
          use: [
            {
              loader: 'url-loader',
              options: { limit: 15000 }
            },
            'image-webpack-loader?bypassOnDebug'
            ]
        },
        {
          test: require.resolve("pace-progress"),
          use: "imports-loader?define=>false"
        },
      ]
    },
    plugins: [
      new webpack.ProvidePlugin({
        jQuery: 'jquery',
        $: 'jquery',
        'window.jQuery': 'jquery',
        numeral: 'numeral',
        moment: 'moment'
      }),
      styleExtractor,
      new webpack.optimize.CommonsChunkPlugin({
        name: 'vendor',
        minChunks: function (module) {
          // this assumes your vendor imports exist in the node_modules directory
          return module.context && module.context.indexOf('node_modules') !== -1;
        }
      }),
      new webpack.optimize.CommonsChunkPlugin({
        name: 'manifest', //But since there are no more common modules between them we end up with just the runtime code included in the manifest file
        minChunks: Infinity
      }),
      new webpack.HashedModuleIdsPlugin(),
      new WebpackChunkHash(),
      new CopyPlugin([
        // vari problemi di interazione webpack con immagini trumbowyg e leaflet
        { from: 'node_modules/trumbowyg/dist/ui/icons.svg', to: 'trumbowyg-icons.svg' }
        ]),
      new webpack.LoaderOptionsPlugin({
        minimize: true,
        debug: false
      }),
      new HtmlWebpackPlugin({
        alwaysWriteToDisk: true
      }),
      new HtmlHarddiskPlugin(),
      new PreloadWebpackPlugin({
        rel: 'preload',
        include: 'all'
      })
    ],
    resolve: {
      extensions: ['.js'],
      alias: {
        'jquery-browserify': require.resolve('jquery'),
        // 'jquery': require.resolve('jquery'),
        'bootstrap-modal': path.resolve(__dirname, 'node_modules/jschr-bootstrap-modal'),
        'x-editable': path.resolve(__dirname, 'node_modules/x-editable/dist'),
        handlebars: 'handlebars/dist/handlebars.js',
        modernizr$: path.resolve(__dirname, ".modernizrrc")
      },
      modules: [
        path.join(__dirname, 'public/javascripts'),
        path.join(__dirname, 'public/stylesheets'),
        "node_modules"
      ]
    }
  };

if (process.env.NODE_ENV === "production") {
  config.watch = false
  config.devtool = "#source-map"
  config.module.rules = config.module.rules.concat([
    {
      test: /\.css$/,
      loader: styleExtractor.extract({
        fallback: 'style-loader',
        use: [{
          loader: 'css-loader',
          options: {
            sourceMap: true
          }
        }]
      })
    },
  ])
  config.plugins = (config.plugins || []).concat([
    new CleanWebpackPlugin(['public/dist'], {
      exclude: ['versions.json']
    }),
//    new webpack.optimize.AggressiveSplittingPlugin({
//      minSize: 30000,
//      maxSize: 50000
//    }),
    new webpack.DefinePlugin({
      'process.env': {
        NODE_ENV: '"production"'
      }
    }),
    new BundleAnalyzerPlugin({
      analyzerMode: 'static',
      openAnalyzer: false
    }),
    new OptimizeCssAssetsPlugin({
      cssProcessor: cssnano,
      cssProcessorOptions: {
        discardComments: { removeAll: true },
        autoprefixer: {},
        safe: true
      }
    }),
    new BabelMinifyPlugin({}),
    new webpack.optimize.ModuleConcatenationPlugin(),
//    new webpack.optimize.UglifyJsPlugin({
//      sourceMap: true,
//      compress: {
//        warnings: false
//      }
//    }),
    new webpack.optimize.OccurrenceOrderPlugin(),
    new webpack.LoaderOptionsPlugin({
      minimize: true
    })
  ])
} else {
  config.module.rules = config.module.rules.concat([
    {
      test: /\.css$/,
      use: [
        {
          loader: 'style-loader',
          options: {
            sourceMap: true
          }
        },
        {
          loader: 'css-loader',
          options: {
            sourceMap: true
          }
        }
      ]
    }, {
      test: require.resolve('jquery'),
      use: [{
          loader: 'expose-loader',
          options: '$'
      }, {
          loader: 'expose-loader',
          options: 'jQuery'
      }]
    }
  ])
  config.output.filename = "[name].js"; // no chunkhash in development
}

module.exports = config;
